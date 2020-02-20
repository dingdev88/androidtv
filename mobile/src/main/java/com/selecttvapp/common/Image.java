package com.selecttvapp.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.selecttvapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class Image {
    private static ArrayList<ImageView> PicassoImageViews = new ArrayList<>();
    private static ArrayList<ImageView> GlideImageViews = new ArrayList<>();

    public static void loadImage(String image_url, final ImageView imageView) {
        GlideApp.with(imageView.getContext()).load(image_url)
                .fitCenter()
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }

    public static void loadGridImage(String image_url, final ImageView imageView) {
        loadGridImage(imageView.getContext(), image_url, imageView);
    }

    public static void loadGridImage(Context context, String image_url, final ImageView imageView) {
      GlideApp.with(context).load(image_url)
                .placeholder(R.drawable.loader_network)
                .fitCenter()
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadShowImage(String image_url, final ImageView imageView) {
        loadShowImage(imageView.getContext(), image_url, imageView);
    }

    public static void loadShowImage(Context context, String image_url, final ImageView imageView) {
     GlideApp.with(context).load(image_url)
                .placeholder(R.drawable.thumbnail_loading)
                .fitCenter()
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    public static void loadMovieImage(String image_url, final ImageView imageView) {
        loadMovieImage(imageView.getContext(), image_url, imageView, false);
    }

    public static void loadMovieImage(final Context context, String image_url, final ImageView imageView, boolean addToList) {
        if (addToList)
            GlideImageViews.add(imageView);

       GlideApp.with(context).load(image_url)
                .placeholder(R.drawable.loader_network1)
//                .listener(new RequestListener<String, GlideDrawable>() {
//                    @Override
//                    public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFirstResource) {
//                        Glide.clear(imageView);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        return false;
//                    }
//                })
                .dontAnimate()
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    public static void cancelRequests(Context context) {
        try {
//            for (int i = 0; i < PicassoImageViews.size(); i++) {
//                try {
//                    Picasso.with(context).cancelRequest(PicassoImageViews.get(i));
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }

            for (int i = 0; i < GlideImageViews.size(); i++) {
                try {
                   GlideApp.with(context).clear(GlideImageViews.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            PicassoImageViews.clear();
            GlideImageViews.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setImageFromUrl(final String url, final ImageView imageView) {
        Log.e("setImageFromUrl", "::url::" + url);
        try {
            imageView.setImageBitmap(getBitmapFromURL(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            e.printStackTrace();
            return null;
        }
    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
