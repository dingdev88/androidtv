package com.demo.network.model;

import com.google.gson.annotations.SerializedName;


public class Data implements Comparable<Object> {
    @SerializedName("name")
    private String name;
    @SerializedName("alink")
    private String appLink;
    @SerializedName("image")
    private String imageLink;

    private AppInfo appInfo;
    private String appPakage;

    public String getPackageName() {
        return getAppPakage();
    }

    public boolean isAppInstalled() {
        return isAppInstalled;
    }

    public void setIsAppInstalled(boolean isAppInstalled) {
        this.isAppInstalled = isAppInstalled;
    }

    private boolean isAppInstalled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public AppInfo getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(AppInfo appInfo) {
        this.appInfo = appInfo;
    }

    @Override
    public String toString() {
        return "Data{" +
                "name='" + name + '\'' +
                ", appLink='" + appLink + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", appInfo=" + appInfo +
                ", isAppInstalled=" + isAppInstalled +
                '}';
    }

    public void parsePackage() {
        setAppPakage(extractePackageName(getAppLink()));
    }

    private String extractePackageName(String appLink) {
        if (appLink.equalsIgnoreCase("none")) {
            return null;
        }
        String[] packageName = appLink.split("=");
        String[] refinedPackgNameList;
        String refinedPackgName = null;
        if (packageName.length > 1) {
            refinedPackgNameList = packageName[1].split("&");
            refinedPackgName = refinedPackgNameList[0];
        } else {
           // if( packageName.length > 0 )
           // refinedPackgName = packageName[0];
            return null;
        }
        return refinedPackgName;
    }

    public void setAppPakage(String appPakage) {
        this.appPakage = appPakage;
    }

    public String getAppPakage() {
        return appPakage;
    }

    @Override
    public int compareTo(Object o) {
        Data f = (Data) o;
        return getName().compareTo(f.getName());
    }
}
