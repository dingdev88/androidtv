package com.selecttvapp.presentation.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.FavoriteItemListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.episodeDetails.EpisodesListFragment;
import com.selecttvapp.episodeDetails.ShowContentFragment;
import com.selecttvapp.model.Episode;
import com.selecttvapp.model.FavoriteBean;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.fragments.PresenterMyInterest;
import com.selecttvapp.presentation.views.ViewShowListener;
import com.selecttvapp.ui.bean.SeasonsBean;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.helper.ViewPagerSpeedScroller;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.selecttvapp.episodeDetails.ShowDetailsActivity.isActiveTab;

/**
 * Created by Appsolute dev on 28-Nov-17.
 */

public class PresenterShowDetails {
    public static final String PARAM_SHOW_ID = "showid";
    public static final String PARAM_SEASON_ID = "seasonid";
    public static final String PARAM_SEASON_NUMBER = "seasonNumber";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_PAY_MODE = "paymode";
    public static final String PARAM_POSITION = "position";
    protected static final String PARAM_EPISODE = "episode";
    protected static final String PARAM_SEASONS_LIST = "seasons_list";

    private Activity activity;
    private ViewShowListener showListener;
    private FontHelper fontHelper;
//    private Episode episode;
//    private FontHelper fontHelper;

//    private int showId = 0;
//    private int payMode;
//
//    protected abstract void loadShow(Episode episode);
//
//    protected abstract void setFavoriteItem(boolean favoriteItem);

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = getActivity();
//        fontHelper = new FontHelper(getActivity());
//        if (getArguments() != null) {
//            showId = getArguments().getInt(PARAM_SHOW_ID);
//            payMode = getArguments().getInt(PARAM_PAY_MODE);
//        }
//    }

    public PresenterShowDetails(ShowContentFragment fragment) {
        activity = fragment.getActivity();
        showListener = fragment;
        fontHelper = new FontHelper();
    }

    public void setShowListener(ViewShowListener showListener) {
        this.showListener = showListener;
    }

    public SeasonsSectionsPagerAdapter getSeasonsSectionsPagerAdapter(FragmentManager fm, int showId, int payMode, ArrayList<SeasonsBean> seasonsList) {
        return new SeasonsSectionsPagerAdapter(fm, showId, payMode, seasonsList);
    }

    public class SeasonsSectionsPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<SeasonsBean> seasonsList = new ArrayList<>();
        int showId = 0;
        int payMode = Constants.ALL;

        public SeasonsSectionsPagerAdapter(FragmentManager fm, int showId, int payMode, ArrayList<SeasonsBean> seasonsList) {
            super(fm);
            this.showId = showId;
            this.payMode = payMode;
            this.seasonsList = seasonsList;
        }

        @Override
        public Fragment getItem(int position) {
            int seasonId = seasonsList.get(position).getId();
            int seasonNumber = seasonsList.get(position).getSeason_number();
            return EpisodesListFragment.instance(seasonId, seasonNumber, showId, payMode, position);
        }

        @Override
        public int getCount() {
            return seasonsList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String[] arr = seasonsList.get(position).getName().split(" ");
            return arr[0] + "\n" + arr[1];
        }
    }

    public void setScrollSpeed(ViewPager viewpager) {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerSpeedScroller scroller = new ViewPagerSpeedScroller(viewpager.getContext());
            scroller.setDuration(1000);
            mScroller.set(viewpager, scroller);
        } catch (Exception e) {
        }
    }

    public void setPageChangeListener(SmartTabLayout smartTabLayout) {
        final LinearLayout lyTabs = (LinearLayout) smartTabLayout.getChildAt(0);
        changeTabsTitleTypeFace(lyTabs, 0);
        smartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeTabsTitleTypeFace(lyTabs, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void changeTabsTitleTypeFace(final LinearLayout ly, final int position) {
        for (int j = 0; j < ly.getChildCount(); j++) {
            TextView tvTabTitle = (TextView) ly.getChildAt(j);
            if (j == position) setFont(fontHelper.tfMyriadProSemibold, tvTabTitle);
            else setFont(fontHelper.tfMyriadProRegular, tvTabTitle);

            if (isActiveTab != null)
                if (!isActiveTab[j]) {
                    tvTabTitle.setClickable(false);
                    tvTabTitle.setSelected(false);
                }
        }
    }

    public void setFont(String font, View... views) {
        fontHelper.applyFonts(font, views);
    }

    public void setFont(final Typeface tf, View view) {
        fontHelper.applyFont(tf, view);
    }


    public void extractShow(Bundle bundle) {
        try {
            Episode episode = (Episode) bundle.getSerializable(PARAM_EPISODE);
            if (episode != null) {
                ArrayList<SeasonsBean> seasonsList = (ArrayList<SeasonsBean>) bundle.getSerializable(PARAM_SEASONS_LIST);
                if (seasonsList != null)
                    if (seasonsList.size() > 0) {
                        episode.setSeasonsList(seasonsList);
                        isActiveTab = new boolean[seasonsList.size()];
                        for (int i = 0; i < isActiveTab.length; i++)
                            isActiveTab[i] = true;
                    }
                showListener.loadShow(episode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadShowDetails(final int showId, final int payMode) {
        final ProgressHUD mProgressHUD = ProgressHUD.show(activity, "Please Wait...", true, false, null);
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject response = JSONRPCAPI.getShowDetail(showId);
                    JSONArray jsonArray = JSONRPCAPI.getShowSeasons(showId + "");

                    if (response != null) {
                        final Episode episode = new Episode(response);
                        if (jsonArray != null)
                            episode.setSeasonsList(parseSeasonsList(payMode, jsonArray));
                        if (episode != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showListener.loadShow(episode);
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (mProgressHUD.isShowing())
                        mProgressHUD.dismiss();
                }
            }
        });
        thread.start();
    }

    public ArrayList<SeasonsBean> parseSeasonsList(int payMode, final JSONArray jsonArray) {
        final ArrayList<SeasonsBean> seasonsBeans = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject demandMenuItem = jsonArray.getJSONObject(i);
                SeasonsBean seasonsBean = new SeasonsBean(demandMenuItem);

                if (payMode == Constants.FREE) {
                    if (seasonsBean.getFreeLinks())
                        seasonsBeans.add(seasonsBean);
                } else seasonsBeans.add(seasonsBean);
            }

            if (seasonsBeans.size() > 0) {
                isActiveTab = new boolean[seasonsBeans.size()];
                for (int i = 0; i < isActiveTab.length; i++)
                    isActiveTab[i] = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seasonsBeans;
    }

    public void checkIsFavoriteTVShow(int showId) {
        if (RabbitTvApplication.getMyfavoriteList() != null)
            if (RabbitTvApplication.getMyfavoriteList().get("favorite-shows") != null)
                if (RabbitTvApplication.getMyfavoriteList().get("favorite-shows").size() > 0) {
                    ArrayList<FavoriteBean> FAVORITE_TVSHOWS_LIST = RabbitTvApplication.getMyfavoriteList().get("favorite-shows");
                    for (int i = 0; i < FAVORITE_TVSHOWS_LIST.size(); i++) {
                        if (FAVORITE_TVSHOWS_LIST.get(i) != null)
                            if (FAVORITE_TVSHOWS_LIST.get(i).getId() == showId) {
                                showListener.setFavoriteItem(true);
                                break;
                            }
                    }
                }
    }

    public void addFavorite(String showId) {
        PresenterMyInterest.getInstance().addFavoriteItem(activity, "show", showId, new FavoriteItemListener() {
            @Override
            public void onItemAdded() {
                showListener.setFavoriteItem(true);
                showListener.addFavorite();
            }

            @Override
            public void onItemRemoved() {

            }

            @Override
            public void onFailureResponse() {
                showListener.setFavoriteItem(false);
            }
        });
    }

    public void removeFavoriteItem(String showId) {
        PresenterMyInterest.getInstance().removeFavoriteItem(activity, "show", showId, new FavoriteItemListener() {
            @Override
            public void onItemAdded() {

            }

            @Override
            public void onItemRemoved() {
                showListener.setFavoriteItem(false);
            }

            @Override
            public void onFailureResponse() {
                showListener.setFavoriteItem(true);
            }
        });
    }
}
