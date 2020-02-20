package com.selecttvapp.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.selecttvapp.R;


public class SquareLinearLayout extends LinearLayout {
    public SquareLinearLayout(Context context) {
        super(context);
    }

    public SquareLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthDesc = MeasureSpec.getMode(widthMeasureSpec);
        int heightDesc = MeasureSpec.getMode(heightMeasureSpec);
        int size = 0;
        if (widthDesc == MeasureSpec.UNSPECIFIED
                && heightDesc == MeasureSpec.UNSPECIFIED) {
            size = getContext().getResources().getDimensionPixelSize(R.dimen.default_size); // Use your own default size, for example 125dp
        } else if ((widthDesc == MeasureSpec.UNSPECIFIED || heightDesc == MeasureSpec.UNSPECIFIED)
                && !(widthDesc == MeasureSpec.UNSPECIFIED && heightDesc == MeasureSpec.UNSPECIFIED)) {
            //Only one of the dimensions has been specified so we choose the dimension that has a value (in the case of unspecified, the value assigned is 0)
            size = width > height ? width : height;
        } else {
            //In all other cases both dimensions have been specified so we choose the smaller of the two
            size = width > height ? height : width;
        }
        setMeasuredDimension(size, size);
    }}
