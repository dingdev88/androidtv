package com.selecttvapp.ui.helper;

import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.selecttvapp.channels.ChannelCategoryList;
import com.selecttvapp.channels.ChannelScheduler;
import com.selecttvapp.channels.LoaderWebServices;
import com.selecttvapp.channels.WebChannelService;
import com.selecttvapp.prefrence.AppPrefrence;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Lenova on 4/21/2017.
 */

public class MyApplication extends MultiDexApplication {
    private static WebChannelService mWebService;
    public static MyApplication instance=null;
    private static Tracker tracker;
    private static Gson gson = null;

    public static MyApplication getInstance() {
        return instance;
    }

    public static LoaderWebServices getmLoaderWebServices() {
        return mLoaderWebServices;
    }

    public static void setmLoaderWebServices(LoaderWebServices mLoaderWebServices) {
        MyApplication.mLoaderWebServices = mLoaderWebServices;
    }

    private static LoaderWebServices mLoaderWebServices;

    public static ArrayList<HttpURLConnection> getmHttpURLConnection() {
        return mHttpURLConnection;
    }

    public void setmHttpURLConnection(ArrayList<HttpURLConnection> mHttpURLConnection) {
        this.mHttpURLConnection = mHttpURLConnection;
    }

    public static ArrayList<HttpURLConnection> mHttpURLConnection = new ArrayList<>();


    public static WebChannelService getmWebService() {
        return mWebService;
    }

    public static void setmWebService(WebChannelService mWebService) {
        MyApplication.mWebService = mWebService;
    }

    public static ArrayList<ChannelCategoryList> getAllCategorylist() {
        return allCategorylist;
    }

    public static void setAllCategorylist(ArrayList<ChannelCategoryList> allCategorylist) {
        MyApplication.allCategorylist = allCategorylist;
    }

    public static ArrayList<ChannelCategoryList> allCategorylist;

    public static HashMap<String, ArrayList<ChannelScheduler>> getmChannelsList() {
        return mChannelsList;
    }

    public static void setmChannelsList(HashMap<String, ArrayList<ChannelScheduler>> mChannelsList) {
        MyApplication.mChannelsList = mChannelsList;
    }

    public static HashMap<String, ArrayList<ChannelScheduler>> mChannelsList = new HashMap<>();

    public static Gson getGson() {
        return gson;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        MultiDex.install(this);
        ImagePipelineConfig pipelineconfig = ImagePipelineConfig.newBuilder(this)
                .setDownsampleEnabled(true)
                .build();
        Fresco.initialize(this, pipelineconfig);
        allowAccessFileProvider();
        mWebService = new WebChannelService(this);
        mLoaderWebServices = new LoaderWebServices(this);
        initGsonBuilder();
    }

    public static Tracker getTracker() {
//        tracker =GoogleAnalytics.getInstance(instance).newTracker(R.xml.global_tracker);
        tracker =GoogleAnalytics.getInstance(instance).newTracker(AppPrefrence.getInstance().getGoogleAnalyticsTrackingId());
        System.out.println("Track"+tracker);
        return tracker;
    }

    /* access files if target sdk version 23 or above 23*/
    public void allowAccessFileProvider() {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public void initGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }
}
