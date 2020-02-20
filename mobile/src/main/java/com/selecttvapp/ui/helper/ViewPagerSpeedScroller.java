package com.selecttvapp.ui.helper;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by Appsolute dev on 23-Nov-17.
 */

public class ViewPagerSpeedScroller extends Scroller {

    private int mDuration = 5000;

    public void setDuration(int mDuration) {
        this.mDuration = mDuration;
    }

    public ViewPagerSpeedScroller(Context context) {
        super(context);
    }

    public ViewPagerSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
}
