package com.selecttvapp.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by Ocs pl-79(17.2.2016) on 8/23/2016.
 */
public class CustomScrollView extends HorizontalScrollView {
    private boolean enableScrolling = false;
    private ScrollListener mEventListener;

    public boolean isScrolledRight() {
        return isScrolledRight;
    }
    public boolean isEnableScrolling() {
        return enableScrolling;
    }

    public void setScrolledRight(boolean scrolledRight) {
        isScrolledRight = scrolledRight;
    }

    public boolean isScrolledRight;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setEnableScrolling(boolean enableScrolling) {
        this.enableScrolling = enableScrolling;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return isEnableScrolling() && super.onInterceptTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isEnableScrolling() && super.onTouchEvent(ev);
    }

    @Override
    public void onScrollChanged(int w, int h, int ow, int oh) {
        if (w < ow) {
           setScrolledRight(true);
           // Log.d("view  xx:::",":::scrolledright");


        }else{
            setScrolledRight(false);
           // Log.d("view  xx:::",":::scrolledleft");
            if (mEventListener != null) {
                mEventListener.onScrolledRight();
            }
        }
    }
    public interface ScrollListener {
        public void onScrolledRight();
    }

    public void setHorizontalScrollListener(ScrollListener mEventListener) {
        this.mEventListener = mEventListener;
    }

}
