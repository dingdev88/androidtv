package com.selecttvapp.presentation.fragments;

import android.app.Activity;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.CarouselsListener;
import com.selecttvapp.callbacks.LoadMoreCarouselsListener;
import com.selecttvapp.callbacks.OnBackPressedListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.HorizontalListitemBean;
import com.selecttvapp.model.OnDemandList;
import com.selecttvapp.model.Slider;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.parser.Parser;
import com.selecttvapp.presentation.views.ViewODSubscriptions;
import com.selecttvapp.ui.base.BasePresenter;
import com.selecttvapp.ui.helper.CustomPageChangeListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by ocspl-72 on 29/12/17.
 */

public class PresenterODSubscriptions extends BasePresenter<ViewODSubscriptions> {
    public final String SUBSCRIPTIONS_MOVIES = "subscription-movies";
    public final String SUBSCRIPTIONS_SHOWS = "subscription-tv";
    public OnBackPressedListener onBackPressedListener = () -> getViewState().onBackPressed();
    /*carousels listener*/
    public CarouselsListener carouselsListener = new CarouselsListener() {
        @Override
        public void onLoadCarousels(ArrayList<CauroselsItemBean> listItems, String type) {

        }

        @Override
        public void onClickItem(HorizontalListitemBean item) {
        }

        @Override
        public void viewMore(String value) {
            getViewState().onClickViewMore(value);
        }

    };
    /*list load more listener*/
    public LoadMoreCarouselsListener loadMoreListener = (listItems, viewAllId) -> getViewState().onLoadMoreCarousels(listItems, viewAllId);
    private Handler handler = new Handler();
    private FontHelper fontHelper = new FontHelper();
    private ArrayList<Carousel> carousels = new ArrayList<>();
    private ArrayList<Slider> sliders = new ArrayList<>();
    private int payMode = Constants.ALL;


    public PresenterODSubscriptions() {
    }

    public int getPayMode() {
        return payMode;
    }

    public void setPayMode(int payMode) {
        this.payMode = payMode;
    }

    public ArrayList<OnDemandList> getCategories() {
        ArrayList<OnDemandList> categories = new ArrayList<>();
        categories.add(new OnDemandList("", "Movies", SUBSCRIPTIONS_MOVIES, ""));
        categories.add(new OnDemandList("", "TV Shows", SUBSCRIPTIONS_SHOWS, ""));
        return categories;
    }// end of the loadSubscriptionsCategories() method

    public void setGridLayoutManager(RecyclerView recyclerView, int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    public SectionPagerAdapter getSectionPagerAdapter(FragmentManager fm, ArrayList<OnDemandList> mOnDemandCategorylist) {
        return new SectionPagerAdapter(fm, mOnDemandCategorylist);
    }

    public void setOnPageChangeListener(SmartTabLayout smartTabLayout, Activity activity) {
        CustomPageChangeListener pageChangeListener = new CustomPageChangeListener(activity);
        pageChangeListener.setSmartTabLayout(smartTabLayout);
        pageChangeListener.setPageChangeListener(position -> getViewState().onPageChangeListener(position));
    }

    //dev7 api
    public void loadSubscriptions(final String KEY) {
        if (RabbitTvApplication.getSliderBeanHashMap().containsKey(KEY) || RabbitTvApplication.getHorizontalBeanHashMap().containsKey(KEY))
            if (RabbitTvApplication.getSliderBeanHashMap() != null && RabbitTvApplication.getHorizontalBeanHashMap() != null) {
                sliders = RabbitTvApplication.getSliderBeanHashMap().get(KEY);
                carousels = RabbitTvApplication.getHorizontalBeanHashMap().get(KEY);
                getViewState().loadSlidersAndCarousels(sliders, carousels);
                return;
            }

        getViewState().showProgressDialog("Please wait...");
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    final JSONObject json = JSONRPCAPI.getOndemandSubscriptions(KEY);
                    if (json != null) {
                        handler.post(() -> {
                            sliders = Parser.parseSliders(json);
                            carousels = Parser.parseCarousels(json);
                            getViewState().loadSlidersAndCarousels(sliders, carousels);
                            if (sliders.size() > 0)
                                RabbitTvApplication.getSliderBeanHashMap().put(KEY, sliders);
                            if (carousels.size() > 0)
                                RabbitTvApplication.getHorizontalBeanHashMap().put(KEY, carousels);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    getViewState().stopProgressDialog();
                }
            }
        };
        thread.start();
    }

    public void loadViewAllData(final String id, final int LIMIT, final int OFFSET, final boolean canLoadMore, final LoadMoreCarouselsListener loadMoreListener) {
        final ArrayList<CauroselsItemBean> listItems = new ArrayList<>();
        if (!canLoadMore)
            getViewState().showProgressDialog("Please wait..");
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    final JSONArray jsonArray = JSONRPCAPI.getAllCarouselsData(Integer.parseInt(id), LIMIT, OFFSET, payMode);
                    if (jsonArray != null) {
                        if (jsonArray != null && jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                CauroselsItemBean cauroselsItemBean = new CauroselsItemBean(object);
                                listItems.add(cauroselsItemBean);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    getViewState().stopProgressDialog();
                    onLoadedViewMoreList(id, listItems, canLoadMore);
                }
            }
        };
        thread.start();
    }

    private void onLoadedViewMoreList(final String id, final ArrayList<CauroselsItemBean> listItems, final boolean canLoadMore) {
        handler.post(() -> {
            if (canLoadMore)
                loadMoreListener.onLoadCarousels(listItems, id);
            else getViewState().loadViewMoreList(id, listItems);
        });
    }

    public class SectionPagerAdapter extends PagerAdapter {
        private ArrayList<OnDemandList> categoryList;

        public SectionPagerAdapter(FragmentManager fm, ArrayList<OnDemandList> categoryList) {
            this.categoryList = categoryList;
        }

        @Override
        public View instantiateItem(ViewGroup collection, int position) {
            TextView textView = new TextView(getContext());
            return textView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return categoryList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return categoryList.get(position).getName();
        }
    }

}
