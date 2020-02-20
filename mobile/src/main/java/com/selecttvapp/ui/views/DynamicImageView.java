package com.selecttvapp.ui.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Custom ImageView class which has interface callbacks to load images from rest
 *
 * @author nine3_marks
 */
@SuppressLint("AppCompatCustomView")
public class DynamicImageView extends ImageView {

    private String strUrl;
    public Context context;

    public DynamicImageView(Context context) {
        super(context);
        this.context = context;
    }

    public DynamicImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public DynamicImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

//    public void loadRoundedImage(String imageUrl) {
//        Picasso.with(getContext()
//                .getApplicationContext())
//                .load(imageUrl)
//                .fit().centerCrop()
//                .placeholder(com.selecttvapp.R.drawable.progress_animation)
//                .transform(new RoundedTransformation(20, 0)).into(this);
//    }

//    public void loadImage(final String imageUrl) {
//        try {
//            strUrl = imageUrl;
//            Picasso.with(getContext()
//                    .getApplicationContext())
//                    .load(imageUrl)
//                    .fit()
////                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
////                    .skipMemoryCache()
//                    .placeholder(R.drawable.thumbnail_loading).into(this);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void loadImage1(final String imageUrl) {
//        try {
//            strUrl = imageUrl;
//            Picasso.with(getContext()
//                    .getApplicationContext())
//                    .load(imageUrl)
//                    .fit()
////                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
////                    .skipMemoryCache()
//                    .placeholder(R.drawable.loader_network).into(this);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public void loadMovieImage(final String imageUrl) {
//        try {
//            strUrl = imageUrl;
//            Picasso.with(getContext()
//                    .getApplicationContext())
//                    .load(imageUrl)
//                    .fit()
////                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
////                    .skipMemoryCache()
//                    .placeholder(R.drawable.loader_network1)
//                    .error(R.drawable.loader_network1).into(this);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public String getImageurl() {
        return strUrl;
    }

//    public class RoundedTransformation implements
//            com.squareup.picasso.Transformation {
//        private final int radius;
//        private final int margin; // dp
//
//        // radius is corner radii in dp
//        // margin is the board in dp
//        public RoundedTransformation(final int radius, final int margin) {
//            this.radius = radius;
//            this.margin = margin;
//        }
//
//        @Override
//        public Bitmap transform(final Bitmap source) {
//            final Paint paint = new Paint();
//            paint.setAntiAlias(true);
//            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
//                    Shader.TileMode.CLAMP));
//
//            try {
//                Bitmap output = Bitmap.createBitmap(source.getWidth(),
//                        source.getHeight(), Bitmap.Config.ARGB_8888);
//                Canvas canvas = new Canvas(output);
//                canvas.drawRoundRect(new RectF(margin, margin, source.getWidth()
//                        - margin, source.getHeight() - margin), radius, radius, paint);
//
//                if (source != output) {
//                    source.recycle();
//                }
//
//                return output;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return source;
//            }
//        }
//
//        @Override
//        public String key() {
//            return "rounded";
//        }
//    }
}
