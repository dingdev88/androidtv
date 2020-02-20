package com.selecttvapp.ui.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.selecttvapp.common.FontHelper;
import com.selecttvapp.common.InternetConnection;
import com.selecttvapp.common.Utils;
import com.selecttvapp.ui.dialogs.ProgressHUD;

/**
 * Created by ocspl-72 on 26/12/17.
 */

public abstract class BaseFragment<DataBinding extends ViewDataBinding> extends Fragment implements MvpView {
    protected FontHelper fontHelper = new FontHelper();
    private ProgressHUD mProgressHUD;
    private DataBinding binding;

    protected abstract int getContentView();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getContentView(), container, false);
        return getRootView();
    }

    public DataBinding getBinding() {
        return binding;
    }

    public View getRootView() {
        return binding.getRoot();
    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
    }

    @Override
    public void onError(String message) {
        if (!message.isEmpty())
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        if (!message.isEmpty())
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public boolean isNetworkConnected() {
        if (InternetConnection.isConnected(getActivity()))
            return true;
        return false;
    }

    @Override
    public void hideKeyboard() {
        Utils.hideKeyBoard(getActivity());
    }

    @Override
    public void hideKeyboard(View view) {
        Utils.hideKeyBoard(getActivity(), view);
    }

    @Override
    public void showProgressDialog(String message) {
        stopProgressDialog();
        mProgressHUD = ProgressHUD.show(getActivity(), message, true, false, null);
    }

    @Override
    public void stopProgressDialog() {
        if (mProgressHUD != null)
            if (mProgressHUD.isShowing())
                mProgressHUD.dismiss();
    }

    @Override
    public void onDestroyView() {
        if (mProgressHUD != null)
            mProgressHUD.dismiss();
        super.onDestroyView();
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }


    public int getOrientation() {
        return getResources().getConfiguration().orientation;
    }

    public boolean isPortraitView() {
        return getOrientation() == Configuration.ORIENTATION_PORTRAIT;
    }

    public boolean isLandcapeView() {
        return getOrientation() == Configuration.ORIENTATION_LANDSCAPE;
    }

    public int getWidthPixels() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public int getHeightPixels() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public void setFont(final String font, final android.view.View... views) {
        fontHelper.applyFonts(font, views);
    }

    public void setFont(final Typeface tf, android.view.View view) {
        fontHelper.applyFont(tf, view);
    }
}
