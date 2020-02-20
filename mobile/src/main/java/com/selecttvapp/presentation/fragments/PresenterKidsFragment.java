package com.selecttvapp.presentation.fragments;

import android.app.Activity;
import android.os.Handler;

import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.KidsPage;
import com.selecttvapp.model.Slider;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.views.ViewKids;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.fragments.KidsFragment;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 23-Nov-17.
 */

public class PresenterKidsFragment {
    private final String KEY_KIDS_SHOWS = "tv-shows-more-channels-kids-and-family";
    private final String KEY_KIDS_MOVIES = "movies-family-and-kids";
    private final String KEY_KIDS_PAY_PER_VIEW = "pay-per-view-kids";

    private Activity activity;
    private ViewKids mListener;
    private KidsPage kidsPage;
    private Handler handler=new Handler();

    public PresenterKidsFragment(KidsFragment fragment) {
        this.activity = fragment.getActivity();
        mListener = fragment;
    }

    //dev7 api
    public void loadKids(final String type) {
        if (RabbitTvApplication.getInstance().getSliderBeanHashMap().containsKey(getKey(type)) || RabbitTvApplication.getInstance().getHorizontalBeanHashMap().containsKey(getKey(type))) {
            ArrayList<Slider> sliders = RabbitTvApplication.getInstance().getSliderBeanHashMap().get(getKey(type));
            ArrayList<Carousel> carousels = RabbitTvApplication.getInstance().getHorizontalBeanHashMap().get(getKey(type));
            mListener.loadContent(sliders, carousels);
        } else {
            final ProgressHUD progressHUD = ProgressHUD.show(activity, "Please wait..", true, false, null);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    if (progressHUD.isShowing())
                        progressHUD.dismiss();
                    try {
                        JSONObject json = JSONRPCAPI.getKidsResponse(getMethod(type), getParams(type));
                        if (json != null) {
                            kidsPage = new KidsPage(json);
                            handler.post(() -> {
                                ArrayList<Slider> sliders = kidsPage.getSliders();
                                ArrayList<Carousel> carousels = kidsPage.getCarousels();
                                if (kidsPage.getSliders().size() > 0)
                                    RabbitTvApplication.getInstance().getSliderBeanHashMap().put(getKey(type), kidsPage.getSliders());
                                if (kidsPage.getCarousels().size() > 0)
                                    RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put(getKey(type), kidsPage.getCarousels());
                                mListener.loadContent(sliders, carousels);
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (progressHUD.isShowing())
                            progressHUD.dismiss();
                    }
                }
            };
            thread.start();
        }
    }

    private String getMethod(String type) {
        switch (type) {
            case "kids_shows":
            case "tv-shows-more-channels-kids-and-family":
            case "kids_movies":
            case "movies-family-and-kids":
                return "pages.get_site_page";
            case "pay-per-view-kids":
                return "pages.get_front_page";
        }
        return "";
    }

    private String getParams(String type) {
        switch (type) {
            case "kids_shows":
            case "tv-shows-more-channels-kids-and-family":
                return "tv-shows-more-channels-kids-and-family";
            case "kids_movies":
            case "movies-family-and-kids":
                return "movies-family-and-kids";
            case "pay-per-view-kids":
                return "pay-per-view-kids";
        }
        return "";
    }

    private String getKey(String type) {
        switch (type) {
            case "kids_shows":
            case "tv-shows-more-channels-kids-and-family":
                return KEY_KIDS_SHOWS;
            case "kids_movies":
            case "movies-family-and-kids":
                return KEY_KIDS_MOVIES;
            case "pay-per-view-kids":
                return KEY_KIDS_PAY_PER_VIEW;
        }
        return "";
    }
}
