package com.selecttvapp.channels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.MediaRouteButton;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;

/**
 * Created by babin on 8/8/2017.
 */

public class MediaRouteButtonHoloDark extends MediaRouteButton {
    public MediaRouteButtonHoloDark(Context context) {
        super(context);
    }

    public MediaRouteButtonHoloDark(Context context, AttributeSet attrs) {
        super(getThemedContext(context), attrs);
    }

    public MediaRouteButtonHoloDark(Context context, AttributeSet attrs, int defStyleAttr) {
        super(getThemedContext(context), attrs, defStyleAttr);
    }
    @SuppressLint("RestrictedApi")
    private static Context getThemedContext(Context context ) {
        context = new ContextThemeWrapper( context, android.support.v7.appcompat.R.style.Theme_AppCompat );
        return new ContextThemeWrapper( context,  android.support.v7.mediarouter.R.style.Theme_MediaRouter );

    }
}