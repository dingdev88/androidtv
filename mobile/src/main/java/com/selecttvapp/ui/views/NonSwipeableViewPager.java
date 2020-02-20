package com.selecttvapp.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Ocs pl-79(17.2.2016) on 10/21/2016.
 */
public class NonSwipeableViewPager extends ViewPager {
    private boolean enabled;

    public NonSwipeableViewPager(Context context) {
        super(context);
        this.enabled = true;
    }

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return this.enabled && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return this.enabled && super.onTouchEvent(event);
    }
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }


}
