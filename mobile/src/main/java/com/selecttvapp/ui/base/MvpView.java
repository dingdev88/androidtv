package com.selecttvapp.ui.base;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.view.View;

/**
 * Created by ocspl-72 on 26/12/17.
 */

public interface MvpView {

    Activity getActivityContext();

    void onError(String message);

    //
    void showMessage(String message);

    //
    void showMessage(@StringRes int resId);

    //
    boolean isNetworkConnected();

    //
    void hideKeyboard();

    void hideKeyboard(View view);

    void showProgressDialog(String message);

    void stopProgressDialog();

    void finishActivity();

}