package com.selecttvapp.ui.base;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.selecttvapp.common.FontHelper;

/**
 * Created by ocspl-72 on 2/1/18.
 */

public abstract class BaseRecyclerViewAdapter<View extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<View> {
    private FontHelper fontHelper = new FontHelper();

    protected abstract Context getContext();

    public void setFont(final String font, final android.view.View... views) {
        fontHelper.applyFonts(font, views);
    }

    public void setFont(final Typeface tf, android.view.View view) {
        fontHelper.applyFont(tf, view);
    }

    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}
