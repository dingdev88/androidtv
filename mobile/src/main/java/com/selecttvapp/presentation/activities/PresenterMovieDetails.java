package com.selecttvapp.presentation.activities;

import android.app.Activity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.FavoriteItemListener;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.model.FavoriteBean;
import com.selecttvapp.model.Movie;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.fragments.PresenterMyInterest;
import com.selecttvapp.presentation.views.ViewMovieListener;
import com.selecttvapp.ui.activities.MovieDetailsActivity;
import com.selecttvapp.ui.dialogs.ProgressHUD;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 23-Nov-17.
 */

public class PresenterMovieDetails {
    private Activity activity;
    private ViewMovieListener mListener;
    private FontHelper fontHelper;

    public PresenterMovieDetails(MovieDetailsActivity activity) {
        this.activity = activity;
        mListener = activity;
        fontHelper = new FontHelper();
    }

    public void loadMovieDetails(final int movieId) {
        final ProgressHUD mProgressHUD = ProgressHUD.show(activity, "Please Wait...", true, false, null);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject response = JSONRPCAPI.getMovieDetail(movieId);
                    if (response == null)
                        return;
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Movie movie = new Movie(response);
                            mListener.onLoadedMovieDetails(movie);
                            mListener.makeAppsLists(response);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (mProgressHUD.isShowing())
                        mProgressHUD.dismiss();
                }
            }
        });
        thread.start();
    }

    public void fillAutoSpacingLayout(String string, FlexboxLayout flexboxLayout) {
        String[] genres = string.split(",");

        for (String text : genres) {
            TextView textView = buildLabel(text.trim());
            flexboxLayout.addView(textView);
        }
    }

    private TextView buildLabel(String text) {
        TextView textView = new TextView(activity);
//        textView.setBackgroundColor(0xFF140EF2);
        textView.setBackgroundColor(activity.getResources().getColor(R.color.light_blue));
        textView.setText(text);
        textView.setTextColor(0xffffffff);
//        textView.setPadding(5, 0, 5, 0);
        textView.setGravity(Gravity.START);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setLayoutParams(createDefaultLayoutParams());
        setFont(FontHelper.MYRIADPRO_SEMIBOLD, textView);
        return textView;
    }

    private FlexboxLayout.LayoutParams createDefaultLayoutParams() {
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 5, 5, 5);
        return lp;
    }

    public void setFont(String font, View... views) {
        fontHelper.applyFonts(font, views);
    }

    public void checkIsFavoriteMovie(int movieId) {
        try {
            if (RabbitTvApplication.getMyfavoriteList() != null) {
                if (RabbitTvApplication.getMyfavoriteList().get("favorite-movies") != null)
                    if (RabbitTvApplication.getMyfavoriteList().get("favorite-movies").size() > 0) {
                        ArrayList<FavoriteBean> FAVORITE_MOVIES_LIST = RabbitTvApplication.getMyfavoriteList().get("favorite-movies");
                        for (int i = 0; i < FAVORITE_MOVIES_LIST.size(); i++) {
                            if (FAVORITE_MOVIES_LIST.get(i) != null)
                                if (FAVORITE_MOVIES_LIST.get(i).getId() == movieId) {
                                    mListener.setFavoriteItem(true);
                                    return;
                                }
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mListener.setFavoriteItem(false);
    }

    public void addFavoriteItem(String movieId) {
        PresenterMyInterest.getInstance().addFavoriteItem(activity, "movie", movieId, new FavoriteItemListener() {
            @Override
            public void onItemAdded() {
                mListener.setFavoriteItem(true);
                mListener.addFavorite();
            }

            @Override
            public void onItemRemoved() {

            }

            @Override
            public void onFailureResponse() {
                mListener.setFavoriteItem(false);
            }
        });
    }

    public void removeFavoriteItem(String movieId) {
        PresenterMyInterest.getInstance().removeFavoriteItem(activity, "movie", movieId, new FavoriteItemListener() {
            @Override
            public void onItemAdded() {

            }

            @Override
            public void onItemRemoved() {
                mListener.setFavoriteItem(false);
            }

            @Override
            public void onFailureResponse() {
                mListener.setFavoriteItem(true);
            }
        });
    }
}
