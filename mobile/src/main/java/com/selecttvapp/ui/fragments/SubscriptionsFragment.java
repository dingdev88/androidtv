package com.selecttvapp.ui.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.selecttvapp.R;
import com.selecttvapp.SlidersAndCarousels.CarouselsPagerFragment;
import com.selecttvapp.SlidersAndCarousels.SlidersPagerFragment;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.databinding.FragmentSubscriptionsBinding;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.OnDemandList;
import com.selecttvapp.model.Slider;
import com.selecttvapp.presentation.fragments.PresenterODSubscriptions;
import com.selecttvapp.presentation.views.ViewODSubscriptions;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.adapters.GridAdapter;
import com.selecttvapp.ui.base.BaseFragment;

import java.util.ArrayList;


public class SubscriptionsFragment extends BaseFragment implements ViewODSubscriptions {
    private static final String ARG_PARAM_PAGETYPE = "page_type";
    private static final String ARG_PARAM_CATEGORYITEM = "category_item";
    private static final String ARG_PARAM_SUBCATEGORYITEM = "sub_category_item";
    private final int VIEWTYPE_HOME = 0;
    private final int VIEWTYPE_OFF = 100;
    private final int VIEWTYPE_VIEW_MORE = 1;
    private final int VIEWTYPE_GRID = 2;
    private final int VIEWTYPE_NETWORK_DETAIL = 3;
    private View toolbarSearch;

    protected PresenterODSubscriptions presenter = new PresenterODSubscriptions();
    private FragmentSubscriptionsBinding binding;
    private PresenterODSubscriptions.SectionPagerAdapter mSectionsPagerAdapter;
    private String mMAIN_CONTENT = "";

    private ArrayList<OnDemandList> mCategorylist = new ArrayList<>();
    private int pagePosition = 0;
    private int PREVIOUS_VIEW = -1;

    public SubscriptionsFragment() {
    }

    public static SubscriptionsFragment newInstance(String categoryItem, String subCategoryItem) {
        SubscriptionsFragment fragment = new SubscriptionsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PARAM_CATEGORYITEM, categoryItem);
        bundle.putString(ARG_PARAM_SUBCATEGORYITEM, subCategoryItem);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected int getContentView() {
        return R.layout.fragment_subscriptions;
    }

    private int getSlidersLayoutId() {
        return R.id.slidersLayout;
    }

    private int getCarouselsLayoutId() {
        return R.id.carouselsLayout;
    }

    private int getNetworkLayoutId() {
        return R.id.networkLayout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onAttach(this);

        if (getArguments() != null) {
            mMAIN_CONTENT = getArguments().getString(ARG_PARAM_CATEGORYITEM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        binding = (FragmentSubscriptionsBinding) getBinding();
        binding.nonSwipeableViewPager.setPagingEnabled(false);
        binding.smartTabLayout.setFocusableInTouchMode(true);
        binding.carouselsLayout.setVisibility(View.GONE);
        binding.slidersLayout.setVisibility(View.GONE);
        binding.layoutTools.setVisibility(View.GONE);


        //change layout view orientation
        onConfigurationChanged(getResources().getConfiguration());
        return getRootView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadCategories(presenter.getCategories());
        toolbarSearch=((HomeActivity)getActivity()).getFocusOnToolbarSearch();
        toolbarSearch.setNextFocusDownId(R.id.pager_item_ondemand_suggestions);
    }
    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.SUBSCRIPTIONS_SCREEN);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getActivity() == null)
            return;
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.rootView.setOrientation(LinearLayout.VERTICAL);
            ((LinearLayout.LayoutParams) binding.upperLayout.getLayoutParams()).weight = 0;
            ((LinearLayout.LayoutParams) binding.upperLayout.getLayoutParams()).setMargins(0, 0, 0, 0);
            binding.slidersLayout.getLayoutParams().height = (getHeightPixels() / 100) * 35;
        } else {
            binding.rootView.setOrientation(LinearLayout.HORIZONTAL);
            ((LinearLayout.LayoutParams) binding.upperLayout.getLayoutParams()).weight = 1;
            ((LinearLayout.LayoutParams) binding.upperLayout.getLayoutParams()).setMargins(0, 0, 5, 0);
            binding.slidersLayout.getLayoutParams().height = 500;
        }
    }

    @Override
    public void loadCategories(ArrayList<OnDemandList> categoryList) {
        try {
            if (categoryList != null && categoryList.size() > 0) {
                mCategorylist = categoryList;
                binding.nonSwipeableViewPager.setOffscreenPageLimit(categoryList.size());
                mSectionsPagerAdapter = presenter.getSectionPagerAdapter(getActivity().getSupportFragmentManager(), categoryList);
                binding.nonSwipeableViewPager.setAdapter(mSectionsPagerAdapter);
                binding.smartTabLayout.setViewPager(binding.nonSwipeableViewPager);
                presenter.setOnPageChangeListener(binding.smartTabLayout,getActivity());
                loadContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageChangeListener(int position) {
        removeSlidersAndCarousels();
        removeNetworkLayout();
        setViewType(VIEWTYPE_OFF);
        loadContent();
    }


    @Override
    public void loadSlidersAndCarousels(ArrayList<Slider> sliders, ArrayList<Carousel> carousels) {
        setSlider(sliders);
        setCarousels(carousels);
    }


    @Override
    public void loadNetworkDetails(String networkId) {
        HomeActivity.setOnBackPressedListener(presenter.onBackPressedListener);
        Fragment fragment = NetworkDetailsFragment.newInstance(networkId, Constants.ALL);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(getNetworkLayoutId(), fragment, NetworkDetailsFragment.TAG).commit();
        binding.networkLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadViewMoreList(final String viewAllId, ArrayList<CauroselsItemBean> listItems) {
        PREVIOUS_VIEW = VIEWTYPE_HOME;
        setViewType(VIEWTYPE_VIEW_MORE);
        final GridAdapter gridAdapter = setGridItems("", listItems);
        if (gridAdapter != null) {
            gridAdapter.setRecyclerview(binding.gridView);
            gridAdapter.setOnLoadMoreListener(new GridAdapter.OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    int OFFSET = gridAdapter.getItemCount() - 1;
                    presenter.loadViewAllData(viewAllId, 100, OFFSET, true, presenter.loadMoreListener);
                }
            });
        }
    }

    @Override
    public void onLoadMoreCarousels(ArrayList<CauroselsItemBean> listItems, String viewAllId) {
        if (binding.gridView != null)
            if (binding.gridView.getAdapter() != null)
                if (binding.gridView.getAdapter() instanceof GridAdapter) {
                    GridAdapter gridAdapter = (GridAdapter) binding.gridView.getAdapter();
                    gridAdapter.setMoreData(listItems);
                }
    }

    @Override
    public void onClickViewMore(String itemId) {
        setViewType(VIEWTYPE_OFF);
        presenter.loadViewAllData(itemId, 100, 0, false, presenter.loadMoreListener);
    }


    @Override
    public void onBackPressed() {
        removeNetworkLayout();
        if (PREVIOUS_VIEW != -1) {
            setViewType(PREVIOUS_VIEW);

            if (PREVIOUS_VIEW == VIEWTYPE_VIEW_MORE)
                PREVIOUS_VIEW = VIEWTYPE_HOME;
        }
    }

    private void setSlider(ArrayList<Slider> sliders) {
        if (binding.nonSwipeableViewPager.getCurrentItem() != pagePosition)
            return;
        try {
            if (sliders != null)
                if (sliders.size() > 0) {
                    if (sliders.size() > 10)
                        sliders.addAll(sliders.subList(0, 10));

                    Fragment fragment = SlidersPagerFragment.newInstance(pagePosition, presenter.getPayMode(), sliders);
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.replace(getSlidersLayoutId(), fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
                    binding.slidersLayout.setVisibility(View.VISIBLE);
                } else binding.slidersLayout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCarousels(ArrayList<Carousel> carousels) {
        if (getActivity() == null)
            return;

        if (binding.nonSwipeableViewPager.getCurrentItem() != pagePosition)
            return;

        if (carousels.size() > 0) {
            Fragment fragment = CarouselsPagerFragment.newInstance(pagePosition, presenter.getPayMode(), carousels, presenter.carouselsListener);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(getCarouselsLayoutId(), fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
            binding.carouselsLayout.setVisibility(View.VISIBLE);
        } else binding.carouselsLayout.setVisibility(View.GONE);
    }

    public GridAdapter setGridItems(final String type, final ArrayList<CauroselsItemBean> listItems) {
        GridAdapter gridAdapter = null;
        String mType = type;
        if (listItems.size() > 0) {
            if (type.isEmpty())
                mType = listItems.get(0).getType();

            if (mType.equalsIgnoreCase("show") || mType.equalsIgnoreCase("s"))
                setGridLayoutManager(binding.gridView, 2);
            else setGridLayoutManager(binding.gridView, 3);

            gridAdapter = new GridAdapter(listItems, presenter.getPayMode(), getActivity(), null);
            binding.gridView.setAdapter(gridAdapter);
            binding.gridView.setVisibility(View.VISIBLE);
            binding.labelNoData.setVisibility(View.GONE);
        } else {
            binding.gridView.setVisibility(View.GONE);
            binding.labelNoData.setVisibility(View.VISIBLE);
        }

        return gridAdapter;
    }

    private void setGridLayoutManager(RecyclerView recyclerView, int spanCount) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    private void setViewType(int viewType) {
        switch (viewType) {
            case VIEWTYPE_HOME:
                HomeActivity.setOnBackPressedListener(null);
                PREVIOUS_VIEW = -1;
                binding.networkLayout.setVisibility(View.GONE);
                binding.gridView.setVisibility(View.GONE);
                binding.gridListLayout.setVisibility(View.GONE);
                binding.slidersLayout.setVisibility(View.VISIBLE);
                binding.labelNoData.setVisibility(View.GONE);
                binding.carouselsLayout.setVisibility(View.VISIBLE);
                break;
            case VIEWTYPE_GRID:
                HomeActivity.setOnBackPressedListener(null);
                PREVIOUS_VIEW = -1;
                binding.networkLayout.setVisibility(View.GONE);
                binding.slidersLayout.setVisibility(View.GONE);
                binding.carouselsLayout.setVisibility(View.GONE);
                binding.labelNoData.setVisibility(View.GONE);
                binding.gridListLayout.setVisibility(View.GONE);
                binding.gridView.setVisibility(View.VISIBLE);
                break;
            case VIEWTYPE_VIEW_MORE:
                HomeActivity.setOnBackPressedListener(presenter.onBackPressedListener);
                binding.networkLayout.setVisibility(View.GONE);
                binding.slidersLayout.setVisibility(View.GONE);
                binding.carouselsLayout.setVisibility(View.GONE);
                binding.gridListLayout.setVisibility(View.GONE);
                binding.labelNoData.setVisibility(View.GONE);
                binding.gridView.setVisibility(View.VISIBLE);
                break;
            case VIEWTYPE_OFF:
                HomeActivity.setOnBackPressedListener(null);
                PREVIOUS_VIEW = -1;
                binding.labelNoData.setVisibility(View.GONE);
                binding.networkLayout.setVisibility(View.GONE);
                binding.gridView.setVisibility(View.GONE);
                binding.gridListLayout.setVisibility(View.GONE);
                binding.slidersLayout.setVisibility(View.GONE);
                binding.carouselsLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void removeNetworkLayout() {
        try {
            Fragment fragment = null;
            /*remove network layout*/
            fragment = getChildFragmentManager().findFragmentById(getNetworkLayoutId());
            if (fragment != null && fragment instanceof NetworkDetailsFragment)
                getChildFragmentManager().beginTransaction().remove(fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeSlidersAndCarousels() {
        try {
            Fragment fragment = null;
            /*remove slider layout*/
            fragment = getChildFragmentManager().findFragmentById(getSlidersLayoutId());
            if (fragment != null && fragment instanceof SlidersPagerFragment)
                getChildFragmentManager().beginTransaction().remove(fragment).commit();
            /*remove carousel layout*/
            fragment = getChildFragmentManager().findFragmentById(getCarouselsLayoutId());
            if (fragment != null && fragment instanceof CarouselsPagerFragment)
                getChildFragmentManager().beginTransaction().remove(fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadContent() {
        try {
            setViewType(VIEWTYPE_OFF);
            String categoryTag = "";
            pagePosition = binding.nonSwipeableViewPager.getCurrentItem();

            if (!mMAIN_CONTENT.isEmpty()) {
                String[] strings = mMAIN_CONTENT.split("/");
                switch (strings[0]) {
                    case "subscription-movies":
                    case "subscriptions-movies":
                        categoryTag = presenter.SUBSCRIPTIONS_MOVIES;
                        break;
                    case "subscription-tv":
                        categoryTag = presenter.SUBSCRIPTIONS_SHOWS;
                        break;
                }

                for (int i = 0; i < mCategorylist.size(); i++)
                    if (categoryTag.equalsIgnoreCase(mCategorylist.get(i).getType())) {
                        mMAIN_CONTENT = "";
                        if (i != 0) {
                            binding.nonSwipeableViewPager.setCurrentItem(i, true);
                            return;
                        }
                    }
            } else categoryTag = mCategorylist.get(pagePosition).getType();

            if (!categoryTag.isEmpty())
                presenter.loadSubscriptions(categoryTag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private class FragmentSubscriptionsBinding {
//        public SmartTabLayout smartTabLayout;
//        public NonSwipeableViewPager nonSwipeableViewPager;
//        public RecyclerView gridView;
//        public FrameLayout slidersLayout, carouselsLayout, networkLayout, gridListLayout;
//        public TextView labelNoData;
//        public LinearLayout rootView, layoutTools, upperLayout;
//
//        public void bind(View view) {
//            smartTabLayout = (SmartTabLayout) view.findViewById(R.id.smartTabLayout);
//            nonSwipeableViewPager = (NonSwipeableViewPager) view.findViewById(R.id.nonSwipeableViewPager);
//            gridView = (RecyclerView) view.findViewById(R.id.gridView);
//            slidersLayout = (FrameLayout) view.findViewById(R.id.slidersLayout);
//            carouselsLayout = (FrameLayout) view.findViewById(R.id.carouselsLayout);
//            networkLayout = (FrameLayout) view.findViewById(R.id.networkLayout);
//            gridListLayout = (FrameLayout) view.findViewById(R.id.gridListLayout);
//            rootView = (LinearLayout) view.findViewById(R.id.rootView);
//            layoutTools = (LinearLayout) view.findViewById(R.id.layoutTools);
//            upperLayout = (LinearLayout) view.findViewById(R.id.upperLayout);
//            labelNoData = (TextView) view.findViewById(R.id.labelNoData);
//        }
//    }
}