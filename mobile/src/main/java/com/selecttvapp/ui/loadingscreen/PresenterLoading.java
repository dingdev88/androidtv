package com.selecttvapp.ui.loadingscreen;

/*
 * Created by Pradeep-OCS on 13/12/18.
 */

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.prefrence.AppPrefrence;
import com.selecttvapp.ui.base.BasePresenter;
import com.selecttvapp.ui.splash.BrandsEntity;

/**
 * Created by ocspl-72 on 10/1/18.
 */

public class PresenterLoading extends BasePresenter<ViewLoading> {
    private Handler handler = new Handler();
    private AlertDialog dialog = null;

    public PresenterLoading() {
    }

    public void getBrands() {
        Thread thread = new Thread(() -> {
            BrandsEntity response = JSONRPCAPI.getBrands();

            if (response != null && !TextUtils.isEmpty(response.googleAnalyticsCode)) {
                AppPrefrence.getInstance().setGoogleAnalyticsTrackingId(response.googleAnalyticsCode);
                handler.post(() -> {
                    getViewState().onGotGoogleAnalyticsTrackingId(response.googleAnalyticsCode);
                });
            }
            if (response != null && !TextUtils.isEmpty(response.androidChannels)) {
                AppPrefrence.getInstance().setAndroidChannel(response.androidChannels);

            }
            if (response != null && !TextUtils.isEmpty(response.domain)) {
                AppPrefrence.getInstance().setBrandDomain(response.domain);

            }
            if (response != null && !TextUtils.isEmpty(response.defaultFrontpage)) {
                AppPrefrence.getInstance().setDefaultFrontPage(response.defaultFrontpage);

            }
          /*  if (response != null && !TextUtils.isEmpty(response.homeScreen)) {
                AppPrefrence.getInstance().setDefaultHomeScreen(response.homeScreen);
            }*/
            AppPrefrence.getInstance().setDefaultHomeScreen("20");
        });
        thread.start();
    }

}
