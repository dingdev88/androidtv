package com.demo.network.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;


public class AppUtil {
    public static void loadImageFromAQuery(AQuery aQuery, Context context, final ImageView imageView, String url, final ProgressBar progressBar) {
        if (url != null && !url.equals("")) {
            BitmapAjaxCallback callback = new BitmapAjaxCallback() {
                @Override
                protected void callback(String url, ImageView iv, Bitmap bm,
                                        AjaxStatus status) {
                    if (bm != null) {
                        progressBar.setVisibility(View.GONE);
                        imageView.setImageBitmap(bm);
                    }

                }
            };
            callback.rotate(true);
            aQuery.id(imageView)
                    .progress(progressBar.getId())
                    .image(url, true, true, AppConstants.IMAGE_SCALED_VALUE,
                            0, callback);
//            Picasso.with(context).load(url).into(imageView);
        }

    }

}
