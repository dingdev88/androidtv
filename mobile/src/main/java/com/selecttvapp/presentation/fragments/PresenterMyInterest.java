package com.selecttvapp.presentation.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.FavoriteItemListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.common.PreferenceManager;
import com.selecttvapp.common.Utils;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.FavoriteBean;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.views.ViewMyInterestListener;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.activities.LoginActivity;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.fragments.MyInterestContentFragment;
import com.selecttvapp.ui.fragments.MyInterestFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Appsolute dev on 29-Nov-17.
 */

public class PresenterMyInterest {
    private final String KEY_SHOWS = "favorite-shows";
    private final String KEY_MOVIES = "favorite-movies";
    private final String KEY_MOVIE_GENRES = "favorite-moviegenres";
    private final String KEY_CHANNELS = "favorite-channels";
    private final String KEY_TV_NETWORKS = "favorite-tvnetworks";
    private final String KEY_VIDEO_LBRARIES = "favorite-videolibraries";

    private Activity activity;
    private ViewMyInterestListener mListener;
    private FontHelper fontHelper;
    private static AlertDialog dialog;
    private Handler handler = new Handler(Looper.getMainLooper());

    public PresenterMyInterest() {
    }

    public static PresenterMyInterest getInstance() {
        return new PresenterMyInterest();
    }

    public PresenterMyInterest(MyInterestFragment fragment) {
        activity = fragment.getActivity();
        mListener = fragment;
        fontHelper = new FontHelper();
    }

    public ArrayList<String> getTabs() {
        ArrayList<String> TABS = new ArrayList<>();
        TABS.add(Constants.TAB_TV_SHOWS);
        TABS.add(Constants.TAB_MOVIES);
        TABS.add(Constants.TAB_MOVIE_GENRES);
        TABS.add(Constants.TAB_CHANNELS);
        TABS.add(Constants.TAB_TV_NETWORKS);
        TABS.add(Constants.TAB_VIDEO_LBRARIES);
        return TABS;
    }

    public SectionsPagerAdapter getSectionsPagerAdapter(FragmentManager fragmentManager) {
        return new SectionsPagerAdapter(fragmentManager, getTabs());
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private ArrayList<String> TABS = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<String> TABS) {
            super(fm);
            this.TABS = TABS;
        }

        @Override
        public Fragment getItem(int position) {
            return MyInterestContentFragment.newInstance(getTabs().get(position), getFavoriteList(getTabs().get(position)));
        }

        @Override
        public int getCount() {
            return TABS.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TABS.get(position);
        }
    }

    public ArrayList<FavoriteBean> getFavoriteList(String type) {
        ArrayList<FavoriteBean> favoriteList = new ArrayList<>();
        switch (type) {
            case Constants.TAB_TV_SHOWS:
                favoriteList = RabbitTvApplication.getMyfavoriteList().get("favorite-shows");
                break;
            case Constants.TAB_MOVIES:
                favoriteList = RabbitTvApplication.getMyfavoriteList().get("favorite-movies");
                break;
            case Constants.TAB_MOVIE_GENRES:
                favoriteList = RabbitTvApplication.getMyfavoriteList().get("favorite-moviegenres");
                break;
            case Constants.TAB_CHANNELS:
                favoriteList = RabbitTvApplication.getMyfavoriteList().get("favorite-channels");
                break;
            case Constants.TAB_TV_NETWORKS:
                favoriteList = RabbitTvApplication.getMyfavoriteList().get("favorite-tvnetworks");
                break;
            case Constants.TAB_VIDEO_LBRARIES:
                favoriteList = RabbitTvApplication.getMyfavoriteList().get("favorite-videolibraries");
                break;
        }
        return favoriteList;
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
                HomeActivity.setOnBackPressedListener(null);
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

    public void loadFavoriteList() {
        final ProgressHUD mProgressHUD = ProgressHUD.show(activity, "Please Wait...", true, false, null);
        Thread thread = new Thread(() -> {
            try {
                JSONObject response = JSONRPCAPI.getFavoriteList(PreferenceManager.getAccessToken());
                if (isTokenExpired(response))
                    showSessionExpiredDialog(activity);
                else {
                    final HashMap<String, ArrayList<FavoriteBean>> favoriteList = parseFavoriteList(response);


                    handler.post(() -> mListener.loadFavoriteList(favoriteList));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mProgressHUD.dismiss();
            }
        });
        thread.start();
    }

    public void addFavoriteItem(FavoriteBean item, String type) {
        String key = getKey(type);
        if (item != null && !key.isEmpty())
            RabbitTvApplication.getMyfavoriteList().get(key).add(item);
    }

    public void addFavoriteItem(final Activity activity, final String TYPE, final String itemId, final FavoriteItemListener favoriteItemListener) {
        Thread thread = new Thread(() -> {
            try {
                final JSONObject response = JSONRPCAPI.addToFavorite(PreferenceManager.getAccessToken(), TYPE, itemId);
                handler.post(() -> {
                    try {
                        if (response.has("success")) {
                            if (response.getBoolean("success")) {
                                Toast.makeText(activity, "Successfully Added", Toast.LENGTH_SHORT).show();
                                if (favoriteItemListener != null)
                                    favoriteItemListener.onItemAdded();
                            } else {
                                favoriteItemListener.onFailureResponse();
                            }
                        }

                        if (isTokenExpired(response)) {
                            favoriteItemListener.onFailureResponse();
                            showSessionExpiredDialog(activity);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void removeFavoriteItem(final Activity activity, final String type, final String itemId, final FavoriteItemListener favoriteItemListener) {
        Thread thread = new Thread(() -> {
            try {
                final JSONObject json = JSONRPCAPI.removeFavorite(PreferenceManager.getAccessToken(), type, itemId);
                handler.post(() -> {
                    try {
                        if (json.has("success")) {
                            if (json.getBoolean("success")) {
                                Toast.makeText(activity, "Removed from Favorite List", Toast.LENGTH_SHORT).show();

                                removeFavoriteItemFromList(type, itemId);
                                if (favoriteItemListener != null)
                                    favoriteItemListener.onItemRemoved();
                            }
                        } else {
                            if (favoriteItemListener != null)
                                favoriteItemListener.onFailureResponse();
                        }
                        if (isTokenExpired(json))
                            showSessionExpiredDialog(activity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void removeFavoriteItemFromList(final String type, final String itemId) {
        if (!type.isEmpty()) {
            String key = getKey(type);
            if (!key.isEmpty())
                if (RabbitTvApplication.getMyfavoriteList() != null)
                    if (RabbitTvApplication.getMyfavoriteList().get(key) != null)
                        if (RabbitTvApplication.getMyfavoriteList().get(key).size() > 0) {
                            ArrayList<FavoriteBean> FAVORITE_LIST = RabbitTvApplication.getMyfavoriteList().get(key);
                            for (int i = 0; i < FAVORITE_LIST.size(); i++) {
                                if (FAVORITE_LIST.get(i) != null)
                                    if (key.equalsIgnoreCase(KEY_CHANNELS)) {
                                        if (FAVORITE_LIST.get(i).getSlug().equalsIgnoreCase(itemId)) {
                                            RabbitTvApplication.getMyfavoriteList().get(key).remove(i);
                                            return;
                                        }
                                    } else if (itemId.equalsIgnoreCase(FAVORITE_LIST.get(i).getId() + "")) {
                                        RabbitTvApplication.getMyfavoriteList().get(key).remove(i);
                                        return;
                                    }
                            }
                        }
        }
    }

    public boolean checkIsFavoriteItem(final String type, String itemId) {
        try {
            if (!type.isEmpty()) {
                String key = getKey(type);
                if (!key.isEmpty())
                    if (RabbitTvApplication.getMyfavoriteList() != null) {
                        if (RabbitTvApplication.getMyfavoriteList().get(key) != null)
                            if (RabbitTvApplication.getMyfavoriteList().get(key).size() > 0) {
                                ArrayList<FavoriteBean> FAVORITE_LIST = RabbitTvApplication.getMyfavoriteList().get(key);
                                for (int i = 0; i < FAVORITE_LIST.size(); i++) {
                                    if (FAVORITE_LIST.get(i) != null)
                                        if (key.equalsIgnoreCase(KEY_CHANNELS)) {
                                            if (FAVORITE_LIST.get(i).getSlug().equalsIgnoreCase(itemId))
                                                return true;
                                        } else if (itemId.equalsIgnoreCase(FAVORITE_LIST.get(i).getId() + "")) {
                                            return true;
                                        }
                                }
                            }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getKey(String type) {
        switch (type) {
            case "show":
                return KEY_SHOWS;
            case "movie":
                return KEY_MOVIES;
            case "moviegenre":
                return KEY_MOVIE_GENRES;
            case "api_channel":
                return KEY_CHANNELS;
            case "network":
                return KEY_TV_NETWORKS;
            case "videolibrary":
                return KEY_VIDEO_LBRARIES;
        }
        return "";
    }

    public void showSessionExpiredDialog(final Activity activity) {
        handler.post(() -> showExpiredTokenAlertDialog(activity));
    }

    public void showExpiredTokenAlertDialog(final Activity activity) {
        if (dialog != null)
            if (dialog.isShowing())
                return;

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.app_name));
        builder.setMessage("Invalid or expired token. Login to Continue");
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    dialog.dismiss();
                    Utils.clearPreferenceData(activity);
                    Intent intent = new Intent(activity.getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    activity.startActivity(intent);
                    activity.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog = builder.create();

        dialog.show();
        TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        TextView titleView = (TextView) dialog
                .findViewById(activity.getResources()
                        .getIdentifier("alertTitle", "id",
                                "android"));

        if (titleView != null) {
            titleView.setGravity(Gravity.CENTER);
        }

    }

    public static void dismisDialog() {
        if (dialog != null)
            if (dialog.isShowing())
                dialog.dismiss();
    }

    public static HashMap<String, ArrayList<FavoriteBean>> parseFavoriteList(JSONObject array_favorite_list) {
        HashMap<String, ArrayList<FavoriteBean>> favoriteList = new HashMap<>();
        try {
            ArrayList<FavoriteBean> tvShowsList = new ArrayList<>();
            ArrayList<FavoriteBean> moviesList = new ArrayList<>();
            ArrayList<FavoriteBean> movieGenresList = new ArrayList<>();
            ArrayList<FavoriteBean> channelsList = new ArrayList<>();
            ArrayList<FavoriteBean> tvnetworksList = new ArrayList<>();
            ArrayList<FavoriteBean> videoLibrariesList = new ArrayList<>();

            if (array_favorite_list.has("tvnetworks")) {
                JSONArray tvnetworks_array = array_favorite_list.getJSONArray("tvnetworks");
                if (tvnetworks_array.length() > 0) {
                    for (int i = 0; i < tvnetworks_array.length(); i++) {
                        FavoriteBean favoriteBean = new FavoriteBean(tvnetworks_array.getJSONObject(i));
                        tvnetworksList.add(favoriteBean);
                    }
                    RabbitTvApplication.getMyfavoriteList().put("favorite-tvnetworks", tvnetworksList);
                }
            }

            if (array_favorite_list.has("api_channels")) {
                JSONArray channels_array = array_favorite_list.getJSONArray("api_channels");
                if (channels_array.length() > 0) {
                    for (int i = 0; i < channels_array.length(); i++) {
                        FavoriteBean favoriteBean = new FavoriteBean(channels_array.getJSONObject(i));
                        channelsList.add(favoriteBean);
                    }
                    RabbitTvApplication.getMyfavoriteList().put("favorite-channels", channelsList);

                }
            }

            if (array_favorite_list.has("movies")) {
                JSONArray movies_array = array_favorite_list.getJSONArray("movies");
                if (movies_array.length() > 0) {
                    for (int i = 0; i < movies_array.length(); i++) {
                        FavoriteBean favoriteBean = new FavoriteBean(movies_array.getJSONObject(i));
                        moviesList.add(favoriteBean);
                    }
                    RabbitTvApplication.getMyfavoriteList().put("favorite-movies", moviesList);
                }
            }

            if (array_favorite_list.has("videolibraries")) {
                JSONArray videolibraries_array = array_favorite_list.getJSONArray("videolibraries");
                if (videolibraries_array.length() > 0) {
                    for (int i = 0; i < videolibraries_array.length(); i++) {
                        FavoriteBean favoriteBean = new FavoriteBean(videolibraries_array.getJSONObject(i));
                        videoLibrariesList.add(favoriteBean);
                    }
                    RabbitTvApplication.getMyfavoriteList().put("favorite-videolibraries", videoLibrariesList);
                }
            }

            if (array_favorite_list.has("moviegenres")) {
                JSONArray moviegenres_array = array_favorite_list.getJSONArray("moviegenres");
                if (moviegenres_array.length() > 0) {
                    for (int i = 0; i < moviegenres_array.length(); i++) {
                        FavoriteBean favoriteBean = new FavoriteBean(moviegenres_array.getJSONObject(i));
                        movieGenresList.add(favoriteBean);
                    }
                    RabbitTvApplication.getMyfavoriteList().put("favorite-moviegenres", movieGenresList);
                }
            }

            if (array_favorite_list.has("shows")) {
                JSONArray shows_array = array_favorite_list.getJSONArray("shows");
                if (shows_array.length() > 0) {
                    for (int i = 0; i < shows_array.length(); i++) {
                        FavoriteBean favoriteBean = new FavoriteBean(shows_array.getJSONObject(i));
                        tvShowsList.add(favoriteBean);
                    }
                    RabbitTvApplication.getMyfavoriteList().put("favorite-shows", tvShowsList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (RabbitTvApplication.getMyfavoriteList().size() > 0)
                favoriteList = RabbitTvApplication.getMyfavoriteList();
        }
        return favoriteList;
    }

    public static boolean isTokenExpired(JSONObject response) {
        try {
            if (response.has("name"))
                if (response.getString("name").equalsIgnoreCase("JSONRPCError"))
                    if (response.has("message")) {
                        String message = response.getString("message");
                        if (message.contains("Invalide or expired token") || message.contains("Invalid or expired token")) {
                            return true;
                        }
                    }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ArrayList<CauroselsItemBean> parseArray(JSONArray carousel_array, String type) {
        ArrayList<CauroselsItemBean> parse_list = new ArrayList<>();

        try {
            if (carousel_array != null) {
                Log.d("carousel_array::", "::" + carousel_array);
                if (carousel_array.length() > 0) {
                    for (int i = 0; i < carousel_array.length(); i++) {
                        JSONObject object = carousel_array.getJSONObject(i);
                        CauroselsItemBean cauroselsItemBean = new CauroselsItemBean(object);
                        if (cauroselsItemBean.getType().isEmpty())
                            cauroselsItemBean.setType(type);
                        parse_list.add(cauroselsItemBean);

                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return parse_list;
    }
}
