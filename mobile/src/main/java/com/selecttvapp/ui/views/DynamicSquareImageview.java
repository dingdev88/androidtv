package com.selecttvapp.ui.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Ocs pl-79(17.2.2016) on 4/12/2016.
 */
public class DynamicSquareImageview extends GridViewItem{
    private String strUrl;

    public DynamicSquareImageview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DynamicSquareImageview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public void loadRoundedImage(String imageUrl) {
//        Picasso.with(getContext()
//                .getApplicationContext())
//                .load(imageUrl)
//                .fit().centerCrop()
//                .placeholder(R.drawable.progress_animation)
//                .transform(new RoundedTransformation(20, 0)).into(this);
//    }
//
//    public void loadImage(String imageUrl) {
//
//        strUrl = imageUrl;
//
//        Picasso.with(getContext()
//                .getApplicationContext())
//                .load(imageUrl).transform(new SquareTransform())
//                .fit().centerCrop()
//                .placeholder(R.drawable.thumbnail_loading).into(this);
//    }

    public String getImageurl(){
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
//
//
//    public class SquareTransform implements
//            com.squareup.picasso.Transformation {
//       // dp
//
//        // radius is corner radii in dp
//        // margin is the board in dp
//        public SquareTransform() {
//
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
//                Bitmap output = Bitmap.createBitmap(source.getHeight(),
//                        source.getHeight(), Bitmap.Config.ARGB_8888);
//
//                if (output.getWidth() >= output.getHeight())
//                    output = Bitmap.createBitmap(output, output.getWidth() / 2 - output.getHeight() / 2, 0, output.getHeight(), output.getHeight());
//                else
//                    output = Bitmap.createBitmap(output, 0, output.getHeight() / 2 - output.getWidth() / 2, output.getWidth(), output.getWidth());
//
//
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
