package com.selecttvapp.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.selecttvapp.BuildConfig;
import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.channels.CategoryListener;
import com.selecttvapp.channels.ChannelApiListener;
import com.selecttvapp.channels.ChannelCategoryList;
import com.selecttvapp.channels.ChannelDatabaseMethods;
import com.selecttvapp.channels.ChannelScheduler;
import com.selecttvapp.channels.ChannelUtils;
import com.selecttvapp.channels.ProgramApiListener;
import com.selecttvapp.channels.Programs;
import com.selecttvapp.channels.StreamApiListener;
import com.selecttvapp.channels.Streams;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.InternetConnection;
import com.selecttvapp.common.PreferenceManager;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.HorizontalListitemBean;
import com.selecttvapp.model.KidsPage;
import com.selecttvapp.model.OnDemandList;
import com.selecttvapp.model.Slider;
import com.selecttvapp.model.forceupdate.ForceUpdate;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.network.LoaderWebserviceInterface;
import com.selecttvapp.prefrence.AppPrefrence;
import com.selecttvapp.presentation.fragments.PresenterForceUpdate;
import com.selecttvapp.presentation.fragments.PresenterGames;
import com.selecttvapp.presentation.fragments.PresenterMyInterest;
import com.selecttvapp.presentation.views.ViewForceUpdate;
import com.selecttvapp.ui.base.BaseActivity;
import com.selecttvapp.ui.bean.ListenGridBean;
import com.selecttvapp.ui.bean.More;
import com.selecttvapp.ui.fragments.FeaturedTvShowsFragment;
import com.selecttvapp.ui.fragments.OnDemandSuggestionFragment;
import com.selecttvapp.ui.helper.MyApplication;
import com.selecttvapp.ui.loadingscreen.PresenterLoading;
import com.selecttvapp.ui.loadingscreen.ViewLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.util.ArrayList;


public class LoadingActivity extends BaseActivity implements ViewLoading, ViewForceUpdate {
    ArrayList<ChannelCategoryList> channelMainCategory = new ArrayList<>();
    ArrayList<OnDemandList> mOnDemandCategorylist = new ArrayList<>();
    Context context;
    private RoundCornerProgressBar splash_progress;
    private PresenterLoading presenter = new PresenterLoading();
    private PresenterForceUpdate presenterForceUpdate = new PresenterForceUpdate();
    //    private ArrayList<ChannelSubCategoryList> allchannelList = new ArrayList<>();
    private TextView txt_splash_progress, txt_splash_title;
    private String loadingText = "";
    private ChannelDatabaseMethods mChannelDatabaseMethods;
    private int progressCount = 0;
    private int channeldataloadedCount = 0;
    private int channelSize = 0;
    private boolean haveChannels = false;
    private boolean isActivityActive = false;
    private boolean isAppUpdateAvailable = false;
    private boolean hasInternet = false;
    private LinearLayout ll_no_internet, ll_load_loader, ll_load_image;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_loading;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isNetworkConnected()) {
            hasInternet = true;
        }
        deleteCache(this);
        presenter.onAttach(this);
        presenterForceUpdate.onAttach(this);
        super.onCreate(savedInstanceState);
        context = this;
        presenter.getBrands();
        mChannelDatabaseMethods = new ChannelDatabaseMethods();
        splash_progress = (RoundCornerProgressBar) findViewById(R.id.splash_progress);
        txt_splash_progress = (TextView) findViewById(R.id.txt_splash_progress);
        txt_splash_title = (TextView) findViewById(R.id.txt_splash_title);
        ll_no_internet = (LinearLayout) findViewById(R.id.ll_no_internet);
        ll_load_loader = (LinearLayout) findViewById(R.id.ll_loading);
        ll_load_image = (LinearLayout) findViewById(R.id.ll_ll_loading_Image);
        if (hasInternet) {
            ll_load_loader.setVisibility(View.VISIBLE);
            ll_no_internet.setVisibility(View.INVISIBLE);
            ll_load_image.setVisibility(View.VISIBLE);
        } else {
            ll_load_loader.setVisibility(View.INVISIBLE);
            ll_no_internet.setVisibility(View.VISIBLE);
            ll_load_image.setVisibility(View.INVISIBLE);
        }
        splash_progress.setProgressColor(Color.parseColor("#fce623"));
        splash_progress.setBackgroundColor(0);
        splash_progress.setProgressBackgroundColor(Color.parseColor("#cecece"));
        splash_progress.setRadius(30);
        splash_progress.setMax(100);

        if (presenterForceUpdate != null && InternetConnection.isConnected(getApplicationContext()))
            presenterForceUpdate.checkVersionUpdate(false);
    }

    private void checkupdate() {
        try {
            if (progressCount >= 95) {
                runOnUiThread(() -> {
                    if (!isAppUpdateAvailable) navigatetoHome(95);
                });
            } else {
                Thread.sleep(3000);
                checkupdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadAllDataList() {
        if (InternetConnection.isConnected(context)) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    checkupdate();

                }
            };
            thread.start();

            loadDemandMenuList();
            loadDemandSliderList();
            loadDemandcarousel(Constants.ALL);
            loadShowSliderList();
            loadShowcarousel(Constants.ALL);
            loadMovieSliderList();
            loadMoviecarousel(Constants.ALL);
            loadWebOriginalsSliderList();
            loadWebOriginalcarousel(Constants.ALL);
            loadKidsSliderList();
            loadKidscarousel(Constants.ALL);
            loadPPVMoviesSliderList();
            loadPPVMoviescarousel(Constants.ALL);
            loadPPVShowsSliderList();
            loadPPVShowscarousel(Constants.ALL);
            loadGamecarousel(Constants.ALL);
            loadMoreCarousel(Constants.ALL);
            loadRadioStations(Constants.ALL);
            loadFavoritesList();
            loadChannelcategories();
            loadKidsData("tv-shows-more-channels-kids-and-family", "pages.get_site_page");
            loadKidsData("movies-family-and-kids", "pages.get_site_page");
            loadKidsData("pay-per-view-kids", "pages.get_front_page");
            loadSuggestions();
            //loadFeaturesChannels();
        } else {
            Toast.makeText(context, "No Internet Connection.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOnResume();

    }

    private void loadOnResume() {
        Utilities.googleAnalytics(Constants.LOADING_SCREEN);
        isActivityActive = true;
        progressCount = 0;
        channeldataloadedCount = 0;
        channelSize = 0;
        splash_progress.setProgress(0);
        if (InternetConnection.isConnected(getApplicationContext())) {
            ll_no_internet.setVisibility(View.INVISIBLE);
            ll_load_loader.setVisibility(View.VISIBLE);
            ll_load_image.setVisibility(View.VISIBLE);
            loadAllDataList();
        } else {
            ll_load_loader.setVisibility(View.INVISIBLE);
            ll_load_image.setVisibility(View.INVISIBLE);
            ll_no_internet.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActivityActive = false;
    }

    @Override
    protected void onDestroy() {
        if (presenterForceUpdate != null)
            presenterForceUpdate.stopProgressDialog();
        super.onDestroy();
        isActivityActive = false;
    }

    @Override
    public void onBackPressed() {
        if (MyApplication.getmLoaderWebServices() != null) {
            MyApplication.getmLoaderWebServices().cancelAllRequests();
        }
        if (MyApplication.getmWebService() != null) {
            MyApplication.getmWebService().cancelAllRequests();
        }
        LoadingActivity.this.finish();
    }

    @Override
    public void onGotGoogleAnalyticsTrackingId(String trackingId) {
        Utilities.googleAnalytics(Constants.LOADING_SCREEN);
    }

    private void parseRadioData(JSONArray radio_array) {
        ArrayList<ListenGridBean> listenGenreBeans = new ArrayList<>();

        for (int i = 0; i < radio_array.length(); i++) {
            String strUrl = "", strName = "";
            int strid = 0;
            try {
                JSONObject jsonObject = radio_array.getJSONObject(i);

                if (jsonObject.has("id")) {
                    strid = jsonObject.getInt("id");
                }
                if (jsonObject.has("name")) {
                    strName = jsonObject.getString("name");

                }
                if (jsonObject.has("image")) {
                    strUrl = jsonObject.getString("image");
                }

                listenGenreBeans.add(new ListenGridBean(strid, strName, strUrl));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        RabbitTvApplication.getInstance().getRadioList().clear();
        RabbitTvApplication.getInstance().getRadioList().addAll(listenGenreBeans);
    }

    private void parseMoreData(JSONArray m_jsonGamemore) {
        ArrayList<More> mMoreArray = new ArrayList<>();
        try {
            for (int i = 0; i < m_jsonGamemore.length(); i++) {
                String url = "", display_mode = "", name = "", weight = "", tags = "", device = "", image = "", description = "", id = "", slug = "";

                JSONObject object = m_jsonGamemore.getJSONObject(i);
                if (object.has("url")) {
                    url = object.getString("url");
                }
                if (object.has("display_mode")) {
                    display_mode = object.getString("display_mode");
                }
                if (object.has("name")) {
                    name = object.getString("name");
                }
                if (object.has("weight")) {
                    weight = object.getString("weight");
                }
                if (object.has("tags")) {
                    tags = object.getString("tags");
                }
                if (object.has("device")) {
                    device = object.getString("device");
                }
                if (object.has("image")) {
                    image = object.getString("image");
                }
                if (object.has("description")) {
                    description = object.getString("description");
                }
                if (object.has("id")) {
                    id = object.getString("id");
                }
                if (object.has("slug")) {
                    slug = object.getString("slug");
                }
                mMoreArray.add(new More(url, display_mode, name, weight, tags, device, image, description, id, slug));
            }
            RabbitTvApplication.getInstance().setmMoreArray(mMoreArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseSubscriptions(JSONArray m_jsonSubscriptions) {
        ArrayList<ListenGridBean> subList = new ArrayList<>();
        try {
            for (int i = 0; i < m_jsonSubscriptions.length(); i++) {
                String name = "", image = "";
                int id = 0;
                JSONObject object = m_jsonSubscriptions.getJSONObject(i);
                if (object.has("name")) {
                    name = object.getString("name");
                }
                if (object.has("image")) {
                    image = object.getString("image");
                }
                if (object.has("id")) {
                    id = object.getInt("id");
                }
                subList.add(new ListenGridBean(id, name, image));
            }
            RabbitTvApplication.getInstance().setSubscriptionList(subList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseDemandMenu(JSONArray category_array) {
        mOnDemandCategorylist.clear();
        try {
            String id = "", name = "", type = "", order = "";
            if (category_array.length() > 0) {
                for (int i = 0; i < category_array.length(); i++) {
                    JSONObject demandMenuItem = category_array.getJSONObject(i);
                    if (demandMenuItem.has("name")) {
                        name = demandMenuItem.getString("name");
                        if (name.equalsIgnoreCase("Primetime Anytime")) {
                            name = "Primetime";
                        }
                    }
                    if (demandMenuItem.has("order")) {
                        order = demandMenuItem.getString("order");
                    }
                    if (demandMenuItem.has("id")) {
                        id = demandMenuItem.getString("id");
                    }
                    if (demandMenuItem.has("page")) {
                        type = demandMenuItem.getString("page");
                    }
                    if (!name.equalsIgnoreCase("world")) {
                        mOnDemandCategorylist.add(new OnDemandList(id, name, type, order));
                    }
                }
            }
            RabbitTvApplication.getInstance().mOnDemandCategorylist = mOnDemandCategorylist;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Slider> parseSlider(JSONArray slider_array) {
        ArrayList<Slider> sb = new ArrayList<>();
        try {
            if (slider_array != null) {
                Log.d("slider_array::", "::" + slider_array);
                if (slider_array.length() > 0) {
                    for (int i = 0; i < slider_array.length(); i++) {
                        String description = "", title = "", image = "", name = "", type = "s";
                        int id = 0;
                        JSONObject slider_object = slider_array.getJSONObject(i);
                        if (slider_object.has("description")) {
                            description = slider_object.getString("description");
                        }
                        if (slider_object.has("title")) {
                            title = slider_object.getString("title");
                        }
                        if (slider_object.has("id")) {
                            id = slider_object.getInt("id");
                        }
                        if (slider_object.has("image")) {
                            image = slider_object.getString("image");
                        }
                        if (slider_object.has("name")) {
                            name = slider_object.getString("name");
                        }
                        if (slider_object.has("type")) {
                            type = slider_object.getString("type");
                        }
                        sb.add(new Slider(id, description, title, image, name, type));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb;
    }

    private ArrayList<Carousel> parseCarousel(JSONArray carousel_array) {
        ArrayList<HorizontalListitemBean> horizontalListBeans;
        ArrayList<Carousel> mCarousel = new ArrayList<>();

        try {
            if (carousel_array != null) {
                Log.d("carousel_array::", "::" + carousel_array);
                if (carousel_array.length() > 0) {
                    for (int i = 0; i < carousel_array.length(); i++) {
                        int carousel_id = 0;
                        String carousel_title = "";
                        horizontalListBeans = new ArrayList<>();
                        JSONObject object = carousel_array.getJSONObject(i);
                        if (object.has("id")) {
                            carousel_id = object.getInt("id");
                        }

                        if (object.has("title")) {
                            carousel_title = object.getString("title");
                            Log.d("title title::", ":::" + carousel_title);
                        }
                        if (TextUtils.isEmpty(carousel_title)) {
                            if (object.has("name")) {
                                carousel_title = object.getString("name");
                                Log.d("name ::", ":::" + carousel_title);
                            }
                        }


                        if (object.has("items")) {
                            JSONArray itemsarray = object.getJSONArray("items");
                            for (int j = 0; j < itemsarray.length(); j++) {
                                String carousel_type = "", carousel_itemsname = "", carousel_itemscarousel_image = "";
                                int carousel_itemsid = 0;
                                JSONObject itemsobject = itemsarray.getJSONObject(j);
                                if (itemsobject.has("type")) {
                                    carousel_type = itemsobject.getString("type");
                                }

                                if (itemsobject.has("id")) {
                                    carousel_itemsid = itemsobject.getInt("id");
                                }
                                if (itemsobject.has("name")) {
                                    carousel_itemsname = itemsobject.getString("name");
                                }
                                if (itemsobject.has("carousel_image")) {
                                    carousel_itemscarousel_image = itemsobject.getString("carousel_image");

                                }


                                horizontalListBeans.add(new HorizontalListitemBean(carousel_itemsid, carousel_itemscarousel_image, carousel_type, carousel_itemsname));

                            }
                            if (horizontalListBeans.size() != 0) {
                                mCarousel.add(new Carousel(carousel_id, carousel_title, "", horizontalListBeans));
                            }


                        }
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mCarousel;
    }

    private void loadChannelcategories() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmWebService().loadChannelsCategories(new CategoryListener() {
                    @Override
                    public void onCategoriesLoaded(final ArrayList<ChannelCategoryList> categorylist) {
                        updatedProgressCount();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChannelDatabaseMethods.saveChannelcategoriesList(categorylist);
                                ChannelCategoryList mChannelCategoryList = mChannelDatabaseMethods.getChannelMaincategoriesList().get(0);
                                loadChannelList(mChannelCategoryList.getSlug());
                            }
                        });
                    }

                    @Override
                    public void onLoadingFailed() {
                        updatedProgressCount();
                    }
                });
            }
        };
        thread.start();
    }

    private void loadChannelList(final String slug) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmWebService().loadChannelsDtaa(slug, false, new ChannelApiListener() {
                    @Override
                    public void onChannelListLoaded(final String categorySlug, final ArrayList<ChannelScheduler> channelList, boolean isDialog) {
                        channelSize = channelList.size();
                        haveChannels = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChannelDatabaseMethods.saveChannelList(channelList, slug);
                               /* if(channelList!=null&&channelList.size()>0){
                                    for(int i=0;i<channelList.size();i++){
                                        ChannelScheduler mChannelScheduler=channelList.get(i);
                                        if(mChannelScheduler.getType().equalsIgnoreCase("live/video")||mChannelScheduler.getType().equalsIgnoreCase("live/radio")){
                                            loadStream(categorySlug,mChannelScheduler.getSlug());
                                        }else{
                                            loadProgramList(categorySlug,mChannelScheduler.getSlug());
                                        }
                                    }
                                }*/
                            }
                        });
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                });
            }
        };
        thread.start();
    }

    private void loadProgramList(final String categorySlug, final String slug) {
        int nWidth = 0;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            nWidth = displayMetrics.heightPixels;

        } else {
            nWidth = displayMetrics.widthPixels;
        }
        int tabwidth = nWidth / 3;
        int cellwidth = (nWidth - ChannelUtils.convertDpToPixels(context)) / 3;
        final int numberOfMoves = cellwidth / 5;
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmWebService().loadProgramData(categorySlug, slug, numberOfMoves, new ProgramApiListener() {

                    @Override
                    public void onProgramsLoaded(final String mcategorySlug, final String pSlug, final Programs mPrograms) {
                        channeldataloadedCount++;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChannelDatabaseMethods.updateProgramList(mcategorySlug, pSlug, mPrograms);
                            }
                        });
                    }

                    @Override
                    public void onLoadingFailed() {
                        channeldataloadedCount++;
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                });

            }
        };
        thread.start();
    }

    private void loadStream(final String categorySlug, final String slug) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmWebService().loadSteamData(categorySlug, slug, new StreamApiListener() {
                    @Override
                    public void onStreamLoaded(final String mcategorySlug, final String sSlug, final ArrayList<Streams> mStream) {
                        channeldataloadedCount++;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChannelDatabaseMethods.updateStream(mcategorySlug, sSlug, mStream);
                            }
                        });


                    }

                    @Override
                    public void onLoadingFailed() {
                        channeldataloadedCount++;
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                });
            }
        };
        thread.start();
    }

    private void loadDemandMenuList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.ondemandSideMenu", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray category_array = new JSONArray(result);
                                if (category_array != null) {
                                    Log.d("loadDemandMenuList::", "::" + category_array);
                                    parseDemandMenu(category_array);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                });
            }
        };
        thread.start();
    }

    private void displayNoNetwork() {
        runOnUiThread(() -> {
            dialogForSlowNet();
            //Toast.makeText(getApplicationContext(), "Error in Internet Connection !", Toast.LENGTH_LONG).show();
            //finish();
        });
    }

    private void dialogForSlowNet() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Setting Alert Dialog Title
        alertDialogBuilder.setTitle("Alert..!!!");
        // Icon Of Alert Dialog
        // alertDialogBuilder.setIcon(R.drawable.question);
        // Setting Alert Dialog Message
        alertDialogBuilder.setMessage("Your Internet speed is slow. Try again after some time");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finishAffinity();
            }
        });

        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadAllDataList();
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void loadDemandSliderList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.ondemandSlider", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray slider_array = new JSONArray(result);
                                if (slider_array != null) {
                                    Log.d("loadDemandSliderList::", "::" + slider_array);
                                    ArrayList<Slider> slider_list = new ArrayList<>();
                                    slider_list = parseSlider(slider_array);
                                    RabbitTvApplication.getInstance().getSliderBeanHashMap().put(Constants.FEATURED, slider_list);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                });
            }
        };
        thread.start();
    }

    private void loadDemandcarousel(final int paymode) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.ondemandCarousels", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray carousel_array = new JSONArray(result);
                                if (carousel_array != null) {
                                    ArrayList<Carousel> horizontalListdata = new ArrayList<>();
                                    horizontalListdata = parseCarousel(carousel_array);
                                    RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put(Constants.FEATURED, horizontalListdata);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                }, paymode, "A");
            }
        };
        thread.start();
    }

    private void loadShowSliderList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.showsSlider", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray slider_array = new JSONArray(result);
                                if (slider_array != null) {
                                    Log.d("loadShowSliderList::", "::" + slider_array);
                                    ArrayList<Slider> slider_list = new ArrayList<>();
                                    slider_list = parseSlider(slider_array);
                                    RabbitTvApplication.getInstance().getSliderBeanHashMap().put("tvshows", slider_list);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                });
            }
        };
        thread.start();
    }

    private void loadShowcarousel(final int paymode) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.showsCarousels", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray carousel_array = new JSONArray(result);
                                if (carousel_array != null) {
                                    ArrayList<Carousel> horizontalListdata = new ArrayList<>();
                                    horizontalListdata = parseCarousel(carousel_array);
                                    RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put("tvshows", horizontalListdata);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                }, paymode, "A");
            }
        };
        thread.start();
    }

    private void loadMovieSliderList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.moviesSlider", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray slider_array = new JSONArray(result);
                                if (slider_array != null) {
                                    Log.d("loadMovieSliderList::", "::" + slider_array);
                                    ArrayList<Slider> slider_list = new ArrayList<>();
                                    slider_list = parseSlider(slider_array);
                                    RabbitTvApplication.getInstance().getSliderBeanHashMap().put("movies", slider_list);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                });
            }
        };
        thread.start();
    }

    private void loadMoviecarousel(final int paymode) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.movies", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray carousel_array = new JSONArray(result);
                                if (carousel_array != null) {
                                    ArrayList<Carousel> horizontalListdata = new ArrayList<>();
                                    horizontalListdata = parseCarousel(carousel_array);
                                    for (int i = 0; i < horizontalListdata.size(); i++) {
                                        for (int j = 0; j < horizontalListdata.get(i).getData_list().size(); j++) {
                                            if (horizontalListdata.get(i).getData_list().get(j).getType().trim().equalsIgnoreCase("s"))
                                                horizontalListdata.get(i).getData_list().remove(j);
                                        }
                                    }
                                    RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put("movies", horizontalListdata);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                }, paymode, "A");
            }
        };
        thread.start();
    }

    private void loadWebOriginalsSliderList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selectvbox.getWebSlider", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray slider_array = new JSONArray(result);
                                if (slider_array != null) {
                                    Log.d("ondemandSlider::", "::" + slider_array);
                                    ArrayList<Slider> slider_list = new ArrayList<>();
                                    slider_list = parseSlider(slider_array);
                                    RabbitTvApplication.getInstance().getSliderBeanHashMap().put(Constants.WEB_ORIGINALS, slider_list);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                }, "tv-shows-web-originals");
            }
        };
        thread.start();
    }

    private void loadWebOriginalcarousel(final int paymode) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.getWebCarousels", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray carousel_array = new JSONArray(result);
                                if (carousel_array != null) {
                                    ArrayList<Carousel> horizontalListdata = new ArrayList<>();
                                    horizontalListdata = parseCarousel(carousel_array);
                                    RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put(Constants.WEB_ORIGINALS, horizontalListdata);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                }, "tv-shows-web-originals", paymode, "A");
            }
        };
        thread.start();
    }

    private void loadKidsSliderList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selectvbox.getKidsSlider", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray slider_array = new JSONArray(result);
                                if (slider_array != null) {
                                    Log.d("ondemandSlider::", "::" + slider_array);
                                    ArrayList<Slider> slider_list = new ArrayList<>();
                                    slider_list = parseSlider(slider_array);
                                    RabbitTvApplication.getInstance().getSliderBeanHashMap().put(Constants.KIDS, slider_list);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                });
            }
        };
        thread.start();
    }

    private void loadKidscarousel(final int paymode) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.getKidsCarousels", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray carousel_array = new JSONArray(result);
                                if (carousel_array != null) {
                                    ArrayList<Carousel> horizontalListdata = new ArrayList<>();
                                    horizontalListdata = parseCarousel(carousel_array);
                                    RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put(Constants.KIDS, horizontalListdata);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                }, "kids", paymode, "A");
            }
        };
        thread.start();
    }

    private void loadPPVMoviesSliderList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.ppvMoviesSlider", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray slider_array = new JSONArray(result);
                                if (slider_array != null) {
                                    Log.d("ondemandSlider::", "::" + slider_array);
                                    ArrayList<Slider> slider_list = new ArrayList<>();
                                    slider_list = parseSlider(slider_array);
                                    RabbitTvApplication.getInstance().getSliderBeanHashMap().put("ppvtvmovies", slider_list);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();

                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                });
            }
        };
        thread.start();
    }

    private void loadPPVMoviescarousel(final int paymode) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.ppvMoviesCarousels", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray carousel_array = new JSONArray(result);
                                if (carousel_array != null) {
                                    ArrayList<Carousel> horizontalListdata = new ArrayList<>();
                                    horizontalListdata = parseCarousel(carousel_array);
                                    RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put("ppvtvmovies", horizontalListdata);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();

                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                }, "pay-per-view-movies", "A");
            }
        };
        thread.start();
    }

    private void loadPPVShowsSliderList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.ppvShowsSlider", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray slider_array = new JSONArray(result);
                                if (slider_array != null) {
                                    Log.d("ondemandSlider::", "::" + slider_array);
                                    ArrayList<Slider> slider_list = new ArrayList<>();
                                    slider_list = parseSlider(slider_array);
                                    RabbitTvApplication.getInstance().getSliderBeanHashMap().put("ppvtvshows", slider_list);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();

                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                });
            }
        };
        thread.start();
    }

    private void loadPPVShowscarousel(final int paymode) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("selecttvbox.ppvShowsCarousels", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray carousel_array = new JSONArray(result);
                                if (carousel_array != null) {
                                    ArrayList<Carousel> horizontalListdata = new ArrayList<>();
                                    horizontalListdata = parseCarousel(carousel_array);
                                    RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put("ppvtvshows", horizontalListdata);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();

                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                }, "pay-per-view-shows", "A");
            }
        };
        thread.start();
    }

    private void loadGamecarousel(final int paymode) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("games.carousels", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray carousel_array = new JSONArray(result);
                                if (carousel_array != null) {
                                    PresenterGames presenter = new PresenterGames();
                                    final ArrayList<Carousel> gamesList = presenter.parseGames(carousel_array);
                                    RabbitTvApplication.getInstance().setGamesList(gamesList);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();

                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                }, paymode, "A");
            }
        };
        thread.start();
    }

    private void loadMoreCarousel(final int paymode) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("games.list", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray carousel_array = new JSONArray(result);
                                if (carousel_array != null) {
                                    parseMoreData(carousel_array);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {

                        updatedProgressCount();
                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                }, "A", "more");
            }
        };
        thread.start();
    }

    private void loadRadioStations(final int paymode) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("radio.getGenres", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        JSONRPCAPI.longInfo("radio.getGenres ::" + result);
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONArray) {
                                JSONArray carousel_array = new JSONArray(result);
                                if (carousel_array != null) {
                                    parseRadioData(carousel_array);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();

                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                }, 1000, 0);
            }
        };
        thread.start();
    }

    private void loadFavoritesList() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                MyApplication.getmLoaderWebServices().getResults("favorites.userList", new LoaderWebserviceInterface() {
                    @Override
                    public void onresponseLoaded(String result) {
                        updatedProgressCount();
                        try {
                            Object json = new JSONTokener(result).nextValue();
                            if (json instanceof JSONObject) {
                                JSONObject carousel_array = new JSONObject(result);
                                if (carousel_array != null) {
                                    PresenterMyInterest.getInstance().parseFavoriteList(carousel_array);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void ondataLoadingFailed() {
                        updatedProgressCount();

                    }

                    @Override
                    public void onNetworkError() {
                        displayNoNetwork();
                    }
                }, PreferenceManager.getAccessToken());
            }
        };
        thread.start();
    }

    //live api
//    private void loadKidsData(final String key, final String method) {
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                MyApplication.getmLoaderWebServices().getResults(method, new LoaderWebserviceInterface() {
//                    @Override
//                    public void onresponseLoaded(String result) {
////                        updatedProgressCount();
//                        try {
//                            JSONObject json = new JSONObject(result);
//                            KidsPage kidsPage = new KidsPage(json);
//                            if (kidsPage.getSliders().size() > 0)
//                                RabbitTvApplication.getInstance().getSliderBeanHashMap().put(key, kidsPage.getSliders());
//                            if (kidsPage.getCarousels().size() > 0)
//                                RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put(key, kidsPage.getCarousels());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void ondataLoadingFailed() {
////                        updatedProgressCount();
//                    }
//
//                    @Override
//                    public void onNetworkError() {
//                        displayNoNetwork();
//                    }
//                }, key);
//            }
//        };
//        thread.start();
//    }

    //dev7 api
    private void loadKidsData(final String key, final String method) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    JSONObject json = JSONRPCAPI.getKidsResponse(method, key);
                    if (json != null) {
                        KidsPage kidsPage = new KidsPage(json);
                        if (kidsPage.getSliders().size() > 0)
                            RabbitTvApplication.getInstance().getSliderBeanHashMap().put(key, kidsPage.getSliders());
                        if (kidsPage.getCarousels().size() > 0)
                            RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put(key, kidsPage.getCarousels());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    //dev7 api
    private void loadSuggestions() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    String defaultFrontPage = (AppPrefrence.getInstance().getDefaultFrontPage());
                    final JSONObject json = JSONRPCAPI.getOndemandSuggestions(defaultFrontPage); //updated on 22-02-2019
                    // final JSONObject json = JSONRPCAPI.getOndemandSuggestions("home");
                    if (json != null) {
                        KidsPage kidsPage = new KidsPage(json);
                        if (kidsPage.getSliders().size() > 0)
                            RabbitTvApplication.getInstance().getSliderBeanHashMap().put(OnDemandSuggestionFragment.KEY, kidsPage.getSliders());
                        if (kidsPage.getCarousels().size() > 0)
                            RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put(OnDemandSuggestionFragment.KEY, kidsPage.getCarousels());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void loadFeaturesChannels() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    String defaultFrontPage = (AppPrefrence.getInstance().getDefaultFrontPage());
                    final JSONObject json = JSONRPCAPI.getFeaturedChannels("testbrandadmin"); //updated on 22-02-2019
                    // final JSONObject json = JSONRPCAPI.getOndemandSuggestions("home");
                    if (json != null) {
                        KidsPage kidsPage = new KidsPage(json);
                        if (kidsPage.getSliders().size() > 0)
                            RabbitTvApplication.getInstance().getSliderBeanHashMapFeatured().put(FeaturedTvShowsFragment.KEY, kidsPage.getSliders());
                        if (kidsPage.getCarousels().size() > 0)
                            RabbitTvApplication.getInstance().getHorizontalBeanHashMapFeatured().put(FeaturedTvShowsFragment.KEY, kidsPage.getCarousels());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void updatedProgressCount() {
        runOnUiThread(() -> {
            progressCount += 5;
            if (progressCount > 100)
                progressCount = 100;
            splash_progress.setProgress(progressCount);
            txt_splash_progress.setText((int) progressCount + "%");
        });
    }


    private void navigatetoHome(int progressCount) {
        if (isActivityActive) {
            Intent intent = new Intent(LoadingActivity.this, HomeActivity.class);
            //intent.putExtra("mode", 20);
            startActivity(intent);
            LoadingActivity.this.finish();
        }
    }


    @Override
    public void onAppUpdateAvailable(boolean isAvailable, ForceUpdate update) {
        if (this.progressCount < 100)
            if (isAvailable && !update.version.equalsIgnoreCase(BuildConfig.VERSION_NAME) && !SplashActivity.isAllreadyCheckedAppUpdateAvailable) {
                this.isAppUpdateAvailable = true;
                SplashActivity.isAllreadyCheckedAppUpdateAvailable = true;
                presenterForceUpdate.showUpdateAppDialog(update.releaseText, update.downloadPath, update.forceUpdate);
            }
    }

    @Override
    public void onAppUpdateSkipped() {
        navigatetoHome(this.progressCount);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (presenterForceUpdate != null)
            presenterForceUpdate.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (presenterForceUpdate != null)
            presenterForceUpdate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onTapToRetry(View view) {
        if (InternetConnection.isConnected(LoadingActivity.this)) {
            loadOnResume();
        } else {
            Toast.makeText(LoadingActivity.this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    //deletes the cache folder present in our app
    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
