package com.selecttvapp.common;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.selecttvapp.ui.helper.MyApplication;

/**
 * Created by Anna Manzhula on 05.07.2017.
 * Helper to apply custom font
 */
public class FontHelper {

    public static final String HELVETICA = "font/HelveticaLT.otf";
    public static final String HELVETICA_BOLD = "font/helvetica-bold.ttf";
    public static final String ROBOTO = "font/Roboto-Light.ttf";
    public static final String ARIALREGULAR = "font/Arial-Regular.ttf";
    public static final String OPENSANS = "font/OpenSans.ttf";
    public static final String MYRIADPRO_BOLD = "font/MyriadPro-Bold.otf";
    public static final String MYRIADPRO_ITALIC = "font/MyriadPro-It.otf";
    public static final String MYRIADPRO_REGULAR = "font/MyriadPro-Regular.otf";
    public static final String MYRIADPRO_SEMIBOLD = "font/MyriadPro-Semibold.otf";

    public Typeface tfMyriadProBold, tfMyriadProIt, tfMyriadProRegular, tfMyriadProSemibold;

    public FontHelper() {
        try {
            tfMyriadProBold = Typeface.createFromAsset(MyApplication.getInstance().getAssets(), MYRIADPRO_BOLD);
            tfMyriadProIt = Typeface.createFromAsset(MyApplication.getInstance().getAssets(), MYRIADPRO_ITALIC);
            tfMyriadProRegular = Typeface.createFromAsset(MyApplication.getInstance().getAssets(), MYRIADPRO_REGULAR);
            tfMyriadProSemibold = Typeface.createFromAsset(MyApplication.getInstance().getAssets(), MYRIADPRO_SEMIBOLD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Apply font to view
     * TextView, EditText and Button supported
     *
     * @param style - font name constant
     * @param views - view set
     */
    public void applyFonts(@NonNull String style, View... views) {
        Typeface tf;

        if (TextUtils.isEmpty(style) || views == null || views.length == 0) {
            return;
        } else {
            try {
                tf = Typeface.createFromAsset(MyApplication.getInstance().getAssets(), style);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            if (tf == null)
                return;

            for (View v : views) {
                if (v instanceof TextView) {
                    ((TextView) v).setTypeface(tf);
                } else if (v instanceof EditText) {
                    ((EditText) v).setTypeface(tf);
                } else if (v instanceof Button) {
                    ((Button) v).setTypeface(tf);
                } else {
                    Log.e("fonts_error", " The type of the view is not recognized.");
                }
            }
        }
    }

    public void applyFont(Typeface tf, View v) {
        try {
            if (tf == null)
                return;
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(tf);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(tf);
            } else {
                Log.e("fonts_error", " The type of the view is not recognized.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
