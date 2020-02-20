package com.selecttvapp;

import android.app.Application;
import android.content.Context;

import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.FavoriteBean;
import com.selecttvapp.model.OnDemandList;
import com.selecttvapp.model.SideMenu;
import com.selecttvapp.model.Slider;
import com.selecttvapp.ui.bean.ListenGridBean;
import com.selecttvapp.ui.bean.More;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;


public class RabbitTvApplication extends Application {

    private static RabbitTvApplication instance;
    public static HashMap<String, ArrayList<FavoriteBean>> myfavoriteList = new HashMap<>();
    public static HashMap<String, ArrayList<Slider>> sliderBeanHashMap = new HashMap<>();
    public static HashMap<String, ArrayList<Slider>> sliderBeanHashMapMobileTest = new HashMap<>();
    public static HashMap<String, ArrayList<Carousel>> horizontalBeanHashMap = new HashMap<>();
    public static HashMap<String, ArrayList<Carousel>> horizontalBeanHashMapMobileTest = new HashMap<>();
    public static ArrayList<SideMenu> sideMenus = new ArrayList<>();
    public static ArrayList<OnDemandList> mOnDemandCategorylist;
    public static ArrayList<ListenGridBean> radioList = new ArrayList<>();
    public static ArrayList<More> mMoreArray = new ArrayList<>();
    public static ArrayList<ListenGridBean> subscriptionList;
    public static ArrayList<Carousel> gamesList = new ArrayList<>();

    public static JSONArray all_Subscription;

    public static int paymode = 1;


    @Override
    public void onCreate() {
        super.onCreate();
        RabbitTvApplication.instance = this;
    }

    public static int getPaymode() {
        return paymode;
    }

    public static void setPaymode(int paymode) {
        RabbitTvApplication.paymode = paymode;
    }


    public static ArrayList<OnDemandList> getmOnDemandCategorylist() {
        return mOnDemandCategorylist;
    }

    public static void setmOnDemandCategorylist(ArrayList<OnDemandList> mOnDemandCategorylist) {
        RabbitTvApplication.mOnDemandCategorylist = mOnDemandCategorylist;
    }


    public static HashMap<String, ArrayList<Slider>> getSliderBeanHashMap() {
        return sliderBeanHashMap;
    }

    public static HashMap<String, ArrayList<Slider>> getSliderBeanHashMapFeatured() {
        return sliderBeanHashMapMobileTest;
    }

    public static void setSliderBeanHashMap(HashMap<String, ArrayList<Slider>> sliderBeanHashMap) {
        RabbitTvApplication.sliderBeanHashMap = sliderBeanHashMap;
    }

    public static HashMap<String, ArrayList<Carousel>> getHorizontalBeanHashMap() {
        return horizontalBeanHashMap;
    }

    public static HashMap<String, ArrayList<Carousel>> getHorizontalBeanHashMapFeatured() {
        return horizontalBeanHashMapMobileTest;
    }

    public static void setHorizontalBeanHashMap(HashMap<String, ArrayList<Carousel>> horizontalBeanHashMap) {
        RabbitTvApplication.horizontalBeanHashMap = horizontalBeanHashMap;
    }

    public static ArrayList<ListenGridBean> getSubscriptionList() {
        return subscriptionList;
    }

    public static void setSubscriptionList(ArrayList<ListenGridBean> subscriptionList) {
        RabbitTvApplication.subscriptionList = subscriptionList;
    }

    public static JSONArray getAll_Subscription() {
        return all_Subscription;
    }

    public static void setAll_Subscription(JSONArray all_Subscription) {
        RabbitTvApplication.all_Subscription = all_Subscription;
    }


    public static ArrayList<More> getmMoreArray() {
        return mMoreArray;
    }

    public static void setmMoreArray(ArrayList<More> mMoreArray) {
        RabbitTvApplication.mMoreArray = mMoreArray;
    }


    public static ArrayList<ListenGridBean> getRadioList() {
        return radioList;
    }

    public static void setRadioDetailBeans(ArrayList<ListenGridBean> radioList) {
        RabbitTvApplication.radioList = radioList;
    }

    public static RabbitTvApplication getInstance() {
        return RabbitTvApplication.instance;
    }

    public static Context getAppContext() {
        return RabbitTvApplication.instance;
    }


//	public static ChannelsFragment getChannelsFragment() {
//		return channelsFragment;
//	}
//
//	public static void setChannelsFragment(ChannelsFragment channelsFragment) {
//		RabbitTvApplication.channelsFragment = channelsFragment;
//	}

    public static void setMyfavoriteList(HashMap<String, ArrayList<FavoriteBean>> myfavoriteList) {
        RabbitTvApplication.myfavoriteList = myfavoriteList;
    }

    public static HashMap<String, ArrayList<FavoriteBean>> getMyfavoriteList() {
        return myfavoriteList;
    }


    public static void setSideMenus(ArrayList<SideMenu> sideMenus) {
        RabbitTvApplication.sideMenus = sideMenus;
    }

    public static ArrayList<SideMenu> getSideMenus() {
        return sideMenus;
    }

    public static void setGamesList(ArrayList<Carousel> gamesList) {
        RabbitTvApplication.gamesList = gamesList;
    }

    public static ArrayList<Carousel> getGamesList() {
        return gamesList;
    }
}
