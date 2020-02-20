package com.selecttvapp.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.selecttvapp.R;


/**
 * Created by Appsolute dev on 12-Oct-17.
 */

public class AspectRatioViewPager extends ViewPager {

    private float mRatio = 1f;
    private boolean enabled = true;

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AspectRatioViewPager(Context context) {
        super(context);
    }

    public AspectRatioViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray styled = getContext().obtainStyledAttributes(attrs, R.styleable.AspectRatioViewPager);
            mRatio = styled.getFloat(R.styleable.AspectRatioViewPager_ratio, 1f);
            styled.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * mRatio);
        setMeasuredDimension(width, height);
        measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        if (this.enabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }
}