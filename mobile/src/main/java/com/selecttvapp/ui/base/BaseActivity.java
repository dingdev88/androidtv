package com.selecttvapp.ui.base;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.selecttvapp.channels.LoaderWebServices;
import com.selecttvapp.channels.WebChannelService;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.common.InternetConnection;
import com.selecttvapp.common.Utils;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.helper.MyApplication;

/**
 * Created by ocspl-72 on 26/12/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements MvpView {
    protected FontHelper fontHelper = new FontHelper();
    private ProgressHUD mProgressHUD;

    protected abstract int getLayoutResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
    }


    @Override
    public Activity getActivityContext() {
        return this;
    }


    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public boolean isNetworkConnected() {
        if (InternetConnection.isConnected(this))
            return true;
        return false;
    }

    @Override
    public void hideKeyboard() {
        Utils.hideKeyBoard(this);
    }

    @Override
    public void hideKeyboard(View view) {
        Utils.hideKeyBoard(this, view);
    }

    @Override
    public void showProgressDialog(String message) {
        stopProgressDialog();
        mProgressHUD = ProgressHUD.show(this, message, true, false, null);
    }

    @Override
    public void stopProgressDialog() {
        if (mProgressHUD != null)
            if (mProgressHUD.isShowing())
                mProgressHUD.dismiss();
    }

    @Override
    protected void onDestroy() {
        if (mProgressHUD != null)
            mProgressHUD.dismiss();
        super.onDestroy();
    }

    @Override
    public void finishActivity() {
        finish();
    }

    public LoaderWebServices getmLoaderWebServices() {
        return MyApplication.getmLoaderWebServices();
    }

    public WebChannelService getmWebService() {
        return MyApplication.getmWebService();
    }

    public int getOrientation() {
        return getResources().getConfiguration().orientation;
    }

    public int getWidthPixels() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public int getHeightPixels() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }


    public void setFont(final String font, final android.view.View... views) {
        fontHelper.applyFonts(font, views);
    }

    public void setFont(final Typeface tf, android.view.View view) {
        fontHelper.applyFont(tf, view);
    }
}
