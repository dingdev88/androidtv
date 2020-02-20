package com.selecttvapp.presentation.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.selecttvapp.SortedListHashMap.SortedListHashMap;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.PreferenceManager;
import com.selecttvapp.episodeDetails.EpisodesListFragment;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.views.ViewShowListener;
import com.selecttvapp.ui.adapters.EpisodesAdapter;
import com.selecttvapp.ui.bean.SeasonsBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Appsolute dev on 28-Nov-17.
 */

public class PresenterEpisodesList {
    protected static final String PARAM_SHOW_ID = "showid";
    protected static final String PARAM_PAY_MODE = "paymode";
    protected static final String PARAM_EPISODES_LIST = "episodes_list";

    private Activity activity;
    private ViewShowListener showListener;
    private LoadEpisodes loadEpisodes;
    private EpisodesAdapter episodeAdapter;
    private ArrayList<Thread> threads = new ArrayList<>();
    private boolean activityDestroyed = false;

    public PresenterEpisodesList(EpisodesListFragment fragment) {
        activity = fragment.getActivity();
        showListener = fragment;
    }

    public PresenterEpisodesList() {

    }

    public void extractEpisodes(Bundle bundle) {
        try {
            SortedListHashMap<Integer, SeasonsBean> episodeBeansList = (SortedListHashMap<Integer, SeasonsBean>) bundle.getSerializable(PARAM_EPISODES_LIST);
            if (episodeBeansList != null)
                if (episodeBeansList.size() > 0) {
                    showListener.onLoadedEpisodes(episodeBeansList);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopThreads() {
        try {
            activity = null;
            if (loadEpisodes != null)
                loadEpisodes.cancel(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    public boolean isActivityDestroyed() {
        if (activity == null || activity.isFinishing() || activity.isDestroyed())
            return true;
        return activityDestroyed;
    }

    public void setActivityDestroyed(boolean activityDestroyed) {
        this.activityDestroyed = activityDestroyed;
    }

    public void loadEpisodes(final int showId, final int seasonId, final int seasonNumber, final int payMode) {
//        loadEpisodes = new LoadEpisodes(showId, seasonId, seasonNumber, payMode);
//        loadEpisodes.execute();

//        Thread thread = new Thread(() -> {
//            try {
//                JSONArray jsonArray = JSONRPCAPI.getShowEpisodes(seasonId);
//                if (jsonArray != null && jsonArray.length() > 0) {
//                    setEpisodesList(showId, seasonId, seasonNumber, payMode, jsonArray);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        thread.start();
//        threads.add(thread);

        AsyncTask.execute(() -> {
            try {
                if (activity == null || activity.isFinishing())
                    return;
                if (isActivityDestroyed())
                    return;
                JSONArray jsonArray = JSONRPCAPI.getShowEpisodes(seasonId);
                if (jsonArray != null && jsonArray.length() > 0) {
                    setEpisodesList(showId, seasonId, seasonNumber, payMode, jsonArray);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showListener.onError();
            }
        });
    }


    private void setEpisodesList(int showId, final int seasonId, final int seasonNumber, int payMode, final JSONArray jsonArray) {

        final SortedListHashMap<Integer, SeasonsBean> episodes = new SortedListHashMap<>(null);
        try {

            for (int i = 0; i < jsonArray.length(); i++) {
                if (isActivityDestroyed())
                    return;

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                SeasonsBean season = new SeasonsBean(jsonObject);
                season.setPayMode(payMode);
                season.setShowid(showId);
                if (season.getSeason_id() < 0) season.setSeason_id(seasonId);
                if (season.getSeason_number() < 0) season.setSeason_number(seasonNumber);

                if (payMode == Constants.FREE) {
                    String[] sub_list = PreferenceManager.geSubscribedList();
                    if (season.getFreeLinks()) {
//                        JSONObject response = JSONRPCAPI.getShowLinks(showId, season.getSeason_id(), season.getId());
//                        season.setShowLinks(response.toString());
//                        try {
//                            JSONArray appsLinksResponse = response.getJSONObject("mobile").getJSONObject("android").getJSONArray("authenticated");
//                            if (appsLinksResponse.length() > 0) {
//                                season.setHasAuthenticatedItems(true);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        episodes.add(season.getId(), season);
                    } else {
                        if (sub_list.length > 0 && season.getSubscriptionsList().size() > 0) {
                            for (int j = 0; j < season.getSubscriptionsList().size(); j++)
                                if (Arrays.asList(sub_list).contains(season.getSubscriptionsList().get(j))) {
//                                    JSONObject response = JSONRPCAPI.getShowLinks(showId, season.getSeason_id(), season.getId());
//                                    season.setShowLinks(response.toString());
//                                    try {
//                                        JSONArray appsLinksResponse = response.getJSONObject("mobile").getJSONObject("android").getJSONArray("authenticated");
//                                        if (appsLinksResponse.length() > 0) {
//                                            season.setHasAuthenticatedItems(true);
//                                        }
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
                                    episodes.add(season.getId(), season);
                                    break;
                                }
                        }
                    }
                } else {
//                    JSONObject response = JSONRPCAPI.getShowLinks(showId, season.getSeason_id(), season.getId());
//                    try {
//                        if (response != null) {
//                            season.setShowLinks(response.toString());
//                            JSONArray appsLinksResponse = response.getJSONObject("mobile").getJSONObject("android").getJSONArray("authenticated");
//                            if (appsLinksResponse.length() > 0) {
//                                season.setHasAuthenticatedItems(true);
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                    episodes.add(season.getId(), season);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showListener.onError();
        } finally {
            if (isActivityDestroyed())
                return;
            activity.runOnUiThread(() -> showListener.onLoadedEpisodes(episodes));
        }
    }

    private void setShowDetails(SeasonsBean season) {
        // final SortedListHashMap<Integer,SeasonsBean> episodes = new SortedListHashMap<>(null);
        try {

            if (isActivityDestroyed())
                return;

            int showID = season.getShowid();
            int payMode = season.getPayMode();
            if (payMode == Constants.FREE) {
                String[] sub_list = PreferenceManager.geSubscribedList();
                if (season.getFreeLinks()) {
                    JSONObject response = JSONRPCAPI.getShowLinks(showID, season.getSeason_id(), season.getId());
                    season.setShowLinks(response.toString());
                    try {
                        JSONArray appsLinksResponse = response.getJSONObject("mobile").getJSONObject("android").getJSONArray("authenticated");
                        if (appsLinksResponse.length() > 0) {
                            season.setHasAuthenticatedItems(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //  episodes.add(season.getId(),season);
                } else {
                    if (sub_list.length > 0 && season.getSubscriptionsList().size() > 0) {
                        for (int j = 0; j < season.getSubscriptionsList().size(); j++)
                            if (Arrays.asList(sub_list).contains(season.getSubscriptionsList().get(j))) {
                                JSONObject response = JSONRPCAPI.getShowLinks(showID, season.getSeason_id(), season.getId());
                                season.setShowLinks(response.toString());
                                try {
                                    JSONArray appsLinksResponse = response.getJSONObject("mobile").getJSONObject("android").getJSONArray("authenticated");
                                    if (appsLinksResponse.length() > 0) {
                                        season.setHasAuthenticatedItems(true);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //episodes.add(season.getId(),season);
                                break;
                            }
                    }
                }
            } else {
                JSONObject response = JSONRPCAPI.getShowLinks(showID, season.getSeason_id(), season.getId());
                try {
                    if (response != null) {
                        season.setShowLinks(response.toString());
                        JSONArray appsLinksResponse = response.getJSONObject("mobile").getJSONObject("android").getJSONArray("authenticated");
                        if (appsLinksResponse.length() > 0) {
                            season.setHasAuthenticatedItems(true);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // episodes.add(season.getId(),season);
            }

        } catch (Exception e) {
            e.printStackTrace();
            showListener.onError();
        } finally {
            if (isActivityDestroyed())
                return;
            activity.runOnUiThread(() -> showListener.onSeasonLoaded(season.getId(), season));
            // episodeAdapter.notifyDataSetChanged();
        }
    }

    public class LoadEpisodes extends AsyncTask {
        private int showId;
        private int seasonId;
        private int seasonNumber;
        private int payMode;

        public LoadEpisodes(final int showId, final int seasonId, final int seasonNumber, final int payMode) {
            this.showId = showId;
            this.seasonId = seasonId;
            this.seasonNumber = seasonNumber;
            this.payMode = payMode;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                if (isActivityDestroyed())
                    return null;
                JSONArray jsonArray = JSONRPCAPI.getShowEpisodes(seasonId);
                if (jsonArray != null && jsonArray.length() > 0) {
                    setEpisodesList(showId, seasonId, seasonNumber, payMode, jsonArray);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    public void loadEpisodesData( final SeasonsBean seasonsBean) {
        AsyncTask.execute(() -> {
            try {
                if (activity == null || activity.isFinishing())
                    return;
                if (isActivityDestroyed())
                    return;

                setShowDetails(seasonsBean);

            } catch (Exception e) {
                e.printStackTrace();
                showListener.onError();
            }
        });
    }
}
