package com.selecttvapp.ui.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.demo.network.common.AppFonts;

/**
 * Created by Lenova on 5/4/2017.
 */

public class CustomFontTextView extends TextView {

    public CustomFontTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface MYRIADPRO_REGULAR = Typeface.createFromAsset(context.getAssets(), AppFonts.MYRIADPRO_REGULAR);
        this.setTypeface(MYRIADPRO_REGULAR);
    }
}
