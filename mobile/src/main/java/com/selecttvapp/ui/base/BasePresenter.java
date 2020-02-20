package com.selecttvapp.ui.base;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import com.selecttvapp.ui.helper.MyApplication;


/**
 * Basic class for presenter
 *
 * @param <View>
 */
public abstract class BasePresenter<View extends BaseView> implements MvpPresenter<View> {
    View view;

    public void onAttach(View view) {
        this.view = view;
    }

    public View getViewState() {
        return view;
    }


    public Context getContext() {
        return MyApplication.getInstance();
    }

    public Activity getActivity() {
        return view.getActivityContext();
    }

    public Resources getResources() {
        return MyApplication.getInstance().getResources();
    }
}
