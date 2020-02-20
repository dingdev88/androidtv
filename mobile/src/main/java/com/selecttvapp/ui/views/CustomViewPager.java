package com.selecttvapp.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Lenova on 4/28/2017.
 */

public class CustomViewPager extends ViewPager {

        private int mCurrentPagePosition = 0;

        public CustomViewPager(Context context) {
            super(context);
        }

        public CustomViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            try {
                View child = getChildAt(mCurrentPagePosition);
                if (child != null) {
                    child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    int h = child.getMeasuredHeight();
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        public void reMeasureCurrentPage(int position) {
            mCurrentPagePosition = position;
            requestLayout();
        }
    }