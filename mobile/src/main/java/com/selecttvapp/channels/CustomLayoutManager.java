package com.selecttvapp.channels;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by babin on 7/4/2017.
 */

public class CustomLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public void setHoriScrollEnabled(boolean horiScrollEnabled) {
        isHoriScrollEnabled = horiScrollEnabled;
    }

    private boolean isHoriScrollEnabled = true;

    public CustomLayoutManager(Context context) {
        super(context);
    }

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return isHoriScrollEnabled && super.canScrollHorizontally();
    }
}