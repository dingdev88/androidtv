package com.selecttvapp.ui.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Ocs pl-79(17.2.2016) on 8/16/2016.
 */
public class CustomGridLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public void setHoriScrollEnabled(boolean horiScrollEnabled) {
        isHoriScrollEnabled = horiScrollEnabled;
    }

    private boolean isHoriScrollEnabled = true;

    public CustomGridLayoutManager(Context context) {
        super(context);
    }

    public CustomGridLayoutManager(Context context, int orientation, boolean reverseLayout) {
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
        return isHoriScrollEnabled&&super.canScrollHorizontally();
    }
}
