package com.demo.network.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;

public class UtilityToast {

    public static void showToastLong(final int resouceId) {
        showToast(RabbitTvApplication.getAppContext(), RabbitTvApplication.getAppContext().getResources().getString(resouceId));
    }

    public static void showToastLong(final String text) {
        showToast(RabbitTvApplication.getAppContext(), text);
    }

    public static void showToast(final int resouceId) {
        showToast(RabbitTvApplication.getAppContext(), RabbitTvApplication.getAppContext().getResources().getString(resouceId));
    }

    public static void showToast(final String text) {
        showToast(RabbitTvApplication.getAppContext(), text);
    }

    public static void showToast(final int resouceId, long duration) {
        showToast(RabbitTvApplication.getAppContext(), RabbitTvApplication.getAppContext().getResources().getString(resouceId), duration);
    }

    public static void showToast(final String text, long duration) {
        showToast(RabbitTvApplication.getAppContext(), text, duration);
    }


    public static void showToast(final Context appContext, final int resouceId) {
        showToast(appContext, appContext.getResources().getString(resouceId));
    }

    public static void showToast(final Context appContext, final int resouceId, long duration) {
        showToast(appContext, appContext.getResources().getString(resouceId), duration);
    }

    public static void showToast(final Context appContext, final String message) {
        showToast(appContext, message, Toast.LENGTH_SHORT);
    }

    public static void showToast(final Context appContext, final String message, long duration) {
        showToast(appContext, message, duration);
    }

    public static void showToast(final Context appContext, final String message, final int duration) {
        Toast toast = Toast.makeText(appContext, message, duration);
        View view = toast.getView();
        view.setBackgroundColor(appContext.getResources().getColor(R.color.transparnet_blue_bg_channel_header));
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(appContext.getResources().getColor(R.color.white));
        toast.show();
    }
}
