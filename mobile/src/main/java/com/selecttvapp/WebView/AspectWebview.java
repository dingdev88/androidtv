package com.selecttvapp.WebView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import com.selecttvapp.R;

/**
 * Created by Appsolute dev on 21-Nov-17.
 */

public class AspectWebview extends WebView {

    private static final int DEFAULT_XRATIO = 16;

    private static final int DEFAULT_YRATIO = 9;

    private int xRatio = DEFAULT_XRATIO;

    private int yRatio = DEFAULT_YRATIO;

    public int getXRatio() {
        return xRatio;
    }

    public void setXRatio(int xRatio) {
        this.xRatio = xRatio;
    }

    public int getYRatio() {
        return yRatio;
    }

    public void setYRatio(int yRatio) {
        this.yRatio = yRatio;
    }

    public AspectWebview(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AspectWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AspectWebview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AspectFrameLayout, 0, 0);
        try {
            xRatio = a.getInt(R.styleable.AspectFrameLayout_xRatio, DEFAULT_XRATIO);
            yRatio = a.getInt(R.styleable.AspectFrameLayout_yRatio, DEFAULT_YRATIO);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == View.MeasureSpec.EXACTLY && (heightMode == View.MeasureSpec.AT_MOST || heightMode == View.MeasureSpec.UNSPECIFIED)) {
            setMeasuredDimension(widthSize, (int) ((double) widthSize / xRatio * yRatio));
        } else if (heightMode == View.MeasureSpec.EXACTLY && (widthMode == View.MeasureSpec.AT_MOST || widthMode == View.MeasureSpec.UNSPECIFIED)) {
            setMeasuredDimension((int) ((double) heightSize / yRatio * xRatio), heightSize);
        } else {
            super.setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
