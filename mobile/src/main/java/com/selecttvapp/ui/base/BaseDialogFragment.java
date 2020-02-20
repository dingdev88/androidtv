package com.selecttvapp.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import com.selecttvapp.common.InternetConnection;
import com.selecttvapp.common.Utils;
import com.selecttvapp.ui.dialogs.ProgressHUD;

/**
 * Created by ocspl-72 on 26/12/17.
 */

public abstract class BaseDialogFragment extends DialogFragment implements MvpView {
    private ProgressHUD mProgressHUD;


    protected abstract int getLayoutResId();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        binding = DataBindingUtil.inflate(inflater, getContentView(), container, false);
//        return getRootView();
//    }


    @Override
    public Activity getActivityContext() {
        return getActivity();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
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
    public void onDestroy() {
        if (mProgressHUD != null)
            mProgressHUD.dismiss();
        super.onDestroy();
    }

    @Override
    public void finishActivity() {
        getActivity().finish();
    }

    public int getOrientation() {
        return getResources().getConfiguration().orientation;
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
}
