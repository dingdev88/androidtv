package com.selecttvapp.ui.bean;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ${Madhan} on 9/21/2016.
 */

public class HomeBean {
    private LinearLayout linearLayout;

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public HomeBean(LinearLayout linearLayout, ImageView imageView, TextView textView) {
        this.linearLayout = linearLayout;
        this.imageView = imageView;
        this.textView = textView;
    }

    private ImageView imageView;
    private TextView textView;
}

