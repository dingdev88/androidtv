package com.selecttvapp.ui.splash;

/*
 * Created by Pradeep-OCS on 13/12/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BrandsEntity {
    @SerializedName("domain")
    @Expose
    public String domain;
    @SerializedName("site_name")
    @Expose
    public String siteName;
    @SerializedName("default_frontpage")
    @Expose
    public String defaultFrontpage;
    @SerializedName("support_url")
    @Expose
    public String supportUrl;
    @SerializedName("logo_image_dark")
    @Expose
    public String logoImageDark;
    @SerializedName("logo_image")
    @Expose
    public String logoImage;
    @SerializedName("ios_channels")
    @Expose
    public String iosChannels;
   /* @SerializedName("home_screen")
    @Expose
    public String homeScreen;*/
    @SerializedName("fandango_aid")
    @Expose
    public String fandangoAid;
    @SerializedName("android_channels")
    @Expose
    public String androidChannels;
    @SerializedName("billing_support_url")
    @Expose
    public Object billingSupportUrl;
    @SerializedName("google_analytics_code")
    @Expose
    public String googleAnalyticsCode;
    @SerializedName("sale_page")
    @Expose
    public String salePage;
    @SerializedName("favicon")
    @Expose
    public Object favicon;
    @SerializedName("amazon_aid")
    @Expose
    public String amazonAid;
    @SerializedName("google_analytics_code_android")
    @Expose
    public String googleAnalyticsCodeAndroid;
    @SerializedName("google_analytics_code_ios")
    @Expose
    public String googleAnalyticsCodeIos;
}
