package com.selecttvapp.prefrence;

/*
 * Created by Pradeep-OCS on 13/12/18.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.selecttvapp.common.Constants;
import com.selecttvapp.ui.helper.MyApplication;

public class AppPrefrence {
    private final String PREFERENCE_TYPE = "ApplicationPreference";
    private SharedPreferences preferences = MyApplication.getInstance().getSharedPreferences(PREFERENCE_TYPE, Context.MODE_PRIVATE);
    private SharedPreferences.Editor edit = preferences.edit();
    private Context context = MyApplication.getInstance();
    private static String URL_KEY = "android_Channels";
    private static String BRAND_KEY = "domain_brand";
    private static String DEFAULT_HOME_PAGE = "default_home_page";
    private static String DEFAULT_HOME_SCREEN = "20";

    public static AppPrefrence getInstance() {
        return new AppPrefrence();
    }

    public String getGoogleAnalyticsTrackingId() {
        String id = preferences.getString(Constants.GOOGLE_ANALYTICS_TRACKING_ID, "");
        return id;
    }

    public void setGoogleAnalyticsTrackingId(String trackingId) {
        edit.putString(Constants.GOOGLE_ANALYTICS_TRACKING_ID, trackingId).apply();
    }

    public void setAndroidChannel(String Url) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(URL_KEY, Url);
        editor.commit();
    }

    public String getAndroidChannel() {
        return preferences.getString(URL_KEY, "");
    }

    public void setBrandDomain(String Url) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BRAND_KEY, Url);
        editor.commit();
    }

    public String getBrandDomain() {
        return preferences.getString(BRAND_KEY, "");
    }

    public void setDefaultFrontPage(String Url) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEFAULT_HOME_PAGE, Url);
        editor.commit();
    }


    public String getDefaultFrontPage() {
        return preferences.getString(DEFAULT_HOME_PAGE, "home");
    }

    public String getDefaultHomeScreen() {
        return preferences.getString(DEFAULT_HOME_SCREEN, "20");
    }

    public void setDefaultHomeScreen(String Url) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEFAULT_HOME_SCREEN, Url);
        editor.commit();
    }
}
