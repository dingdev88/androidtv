package com.selecttvapp.presentation.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.R;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.views.ViewSearch;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.activities.SearchResultsActivity;
import com.selecttvapp.ui.adapters.SearchResultsAdapter;
import com.selecttvapp.ui.bean.SearchResultBean;
import com.selecttvapp.ui.bean.SearchResultListBean;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.fragments.SearchFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static com.selecttvapp.common.Constants.ID;

/**
 * Created by Appsolute dev on 29-Nov-17.
 */

public class PresenterSearch {
    private Activity activity;
    private ViewSearch searchListener;
    private FontHelper fontHelper;

    public PresenterSearch(SearchFragment fragment) {
        activity = fragment.getActivity();
        searchListener = fragment;
        fontHelper = new FontHelper();
    }

    public PresenterSearch(SearchResultsActivity activity) {
        this.activity = activity;
        searchListener = activity;
        fontHelper = new FontHelper();
    }

    public PresenterSearch(SearchResultsActivity.SearchResultsFragment fragment) {
        this.activity = fragment.getActivity();
        searchListener = fragment;
        fontHelper = new FontHelper();
    }

    public SearchResultsAdapter.OnClickListener getAdapterClickListener() {
        return onClickListener;
    }

    SearchResultsAdapter.OnClickListener onClickListener = new SearchResultsAdapter.OnClickListener() {
        @Override
        public void onClickItem(SearchResultBean item) {
            if (item.getType().equalsIgnoreCase("n") || item.getType().equalsIgnoreCase("network")) {
                searchListener.loadItem(item);
            } else if (item.getType().equalsIgnoreCase("channel") || item.getType().equalsIgnoreCase("station")) {
                setchannelResult(item.getId(), item.getSlug(), item.getCategories());
            } else if (item.getType().equalsIgnoreCase("radio")) {
                setRadioResult(item.getId());
            }
        }
    };

    private void setchannelResult(int id, String slug, String categories) {
            Intent intent = new Intent(activity, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(ID, id);
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
    }

    private void setRadioResult(int radioId) {
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ID , radioId);
        activity.setResult(202, intent);
        activity.finish();
    }

    public SectionsPagerAdapter getSectionsPagerAdapter(FragmentManager fragmentManager, ArrayList<SearchResultListBean> searchList) {
        return new SectionsPagerAdapter(fragmentManager, searchList);
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<SearchResultListBean> searchList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<SearchResultListBean> searchList) {
            super(fm);
            this.searchList = searchList;
        }

        @Override
        public Fragment getItem(int position) {
            String name = searchList.get(position).getName();
            return SearchResultsActivity.SearchResultsFragment.newInstance(name, searchList.get(position).getData_list());
        }

        @Override
        public int getCount() {
            return searchList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return searchList.get(position).getName() + "(" + searchList.get(position).getId() + ")";
        }
    }

    public void setPageChangeListener(SmartTabLayout smartTabLayout) {
        final LinearLayout lyTabs = (LinearLayout) smartTabLayout.getChildAt(0);
        changeTabsTitleTypeFace(lyTabs, 0);
        smartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                changeTabsTitleTypeFace(lyTabs, position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }

    private void changeTabsTitleTypeFace(final LinearLayout ly, final int position) {
        for (int j = 0; j < ly.getChildCount(); j++) {
            TextView tvTabTitle = (TextView) ly.getChildAt(j);
            if (j == position) setFont(fontHelper.tfMyriadProSemibold, tvTabTitle);
            else setFont(fontHelper.tfMyriadProRegular, tvTabTitle);
        }
    }

    public void setFont(final String font, final View... views) {
        fontHelper.applyFonts(font, views);
    }

    public void setFont(final Typeface tf, View view) {
        fontHelper.applyFont(tf, view);
    }

    public void loadSearchTask(final String searchValue) {
        final ProgressHUD mProgressHUD = ProgressHUD.show(activity, "Search...", true, false, null);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject response = JSONRPCAPI.getSearchResult(searchValue, 100);
                    if (response == null) return;
                    final ArrayList<SearchResultListBean> searchList = parseSearchResponse(response);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (searchList.size() > 0)
                                searchListener.onSuccess(searchList);
                            else searchListener.onError(new NoSuchElementException());
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mProgressHUD.dismiss();
                }
            }
        });
        thread.start();
    }

    private ArrayList<SearchResultListBean> parseSearchResponse(JSONObject response) {
        ArrayList<SearchResultListBean> searchList = new ArrayList<>();
        try {
            /*if (response.has("station")) {
                SearchResultListBean item = getTabItem("station", "Channels", response);
                if (item != null)
                    searchList.add(item);
            }*/
            if (response.has("network")) {
                SearchResultListBean item = getTabItem("network", "Networks", response);
                if (item != null)
                    searchList.add(item);
            }
            if (response.has("show")) {
                SearchResultListBean item = getTabItem("show", "TV Shows", response);
                if (item != null)
                    searchList.add(item);
            }
            

            if (response.has("movie")) {
                SearchResultListBean item = getTabItem("movie", "Movies", response);
                if (item != null)
                    searchList.add(item);
            }

            if (response.has("actor")) {
                SearchResultListBean item = getTabItem("actor", "Actors", response);
                if (item != null)
                    searchList.add(item);
            }

            if (response.has("tvstation")) {
                SearchResultListBean item = getTabItem("tvstation", "TV Stations", response);
                if (item != null)
                    searchList.add(item);
            }

            if (response.has("live")) {
                SearchResultListBean item = getTabItem("live", "Live", response);
                if (item != null)
                    searchList.add(item);
            }




            if (response.has("radio")) {
                SearchResultListBean item = getTabItem("radio", "Radio", response);
                if (item != null)
                    searchList.add(item);
            }


            if (response.has("music")) {
                SearchResultListBean item = getTabItem("music", "Music", response);
                if (item != null)
                    searchList.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchList;
    }

    private SearchResultListBean getTabItem(String source, String putTabName, JSONObject response) {
        try {
            if (response != null) {
                if (response.has(source)) {
                    ArrayList<SearchResultBean> item_list = new ArrayList<>();
                    JSONObject show_object = response.getJSONObject(source);
                    int count = show_object.getInt("count");
                    if (count > 0) {
                        item_list = parseItems(show_object.getJSONArray("items"));
                    }
                    item_list = filterList(item_list);
                    if (item_list.size() > 0) {
                        SearchResultListBean hlbean = new SearchResultListBean(count, putTabName, source, item_list);
                        return hlbean;
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*filter empty values*/
    private ArrayList<SearchResultBean> filterList(ArrayList<SearchResultBean> listItems) {
        if (listItems.size() > 0) {
            for (int i = listItems.size() - 1; i >= 0; i--) {
                String image_url = listItems.get(i).getImage().trim();
                if (image_url.isEmpty() || image_url == null || image_url.equalsIgnoreCase("null"))
                    listItems.remove(i);
            }
        }
        return listItems;
    }

    private ArrayList<SearchResultBean> parseItems(JSONArray items) {
        ArrayList<SearchResultBean> list = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            try {
                JSONObject jsonObject = items.getJSONObject(i);
                SearchResultBean mSearchResultBean = new SearchResultBean(jsonObject);
                list.add(mSearchResultBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
