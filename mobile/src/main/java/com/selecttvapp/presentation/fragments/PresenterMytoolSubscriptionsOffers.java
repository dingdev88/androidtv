package com.selecttvapp.presentation.fragments;

import android.app.Activity;

import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.Slider;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.views.ViewSubscriptionsOffersListener;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.fragments.MyToolsSubcriptionsOffersFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 23-Nov-17.
 */

public class PresenterMytoolSubscriptionsOffers {

    private Activity activity;
    private ProgressHUD mProgressHUD;
    private ViewSubscriptionsOffersListener mListener;

    public static final String KEY = "mytools_subscription_offers";

    public PresenterMytoolSubscriptionsOffers(MyToolsSubcriptionsOffersFragment fragment) {
        this.activity = fragment.getActivity();
        mListener = fragment;
    }

    //dev7 api
    public void loadSubscriptionsOffers() {
        if (RabbitTvApplication.getInstance().getSliderBeanHashMap().containsKey(KEY) || RabbitTvApplication.getInstance().getHorizontalBeanHashMap().containsKey(KEY)) {
            ArrayList<Slider>  sliders = RabbitTvApplication.getInstance().getSliderBeanHashMap().get(KEY);
            ArrayList<Carousel>   carousels = RabbitTvApplication.getInstance().getHorizontalBeanHashMap().get(KEY);
            mListener.loadContent(sliders, carousels);
        } else {
            final ProgressHUD progressHUD = ProgressHUD.show(activity, "Please wait..", true, false, null);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        final JSONObject json = JSONRPCAPI.loadMyToolsSubscriptionsOffers();
                        if (json != null) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ArrayList<Slider>   sliders = parseSliders(json);
                                    ArrayList<Carousel>    carousels = parseCarousels(json);
                                    if (sliders.size() > 0)
                                        RabbitTvApplication.getInstance().getSliderBeanHashMap().put(KEY, sliders);
                                    if (carousels.size() > 0)
                                        RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put(KEY, carousels);
                                    mListener.loadContent(sliders, carousels);
                                }
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

    private ArrayList<Slider> parseSliders(JSONObject json) {
        ArrayList<Slider> sliders = new ArrayList<>();
        try {
            if (json.has("sliders")) {
                JSONArray slidersJsonArray = json.getJSONArray("sliders");
                for (int i = 0; i < slidersJsonArray.length(); i++) {
                    JSONObject jsonObject = slidersJsonArray.getJSONObject(i);
                    if (jsonObject.has("items")) {
                        JSONArray sliderItemsJsonArray = jsonObject.getJSONArray("items");
                        for (int j = 0; j < sliderItemsJsonArray.length(); j++) {
                            JSONObject jsonObject1 = sliderItemsJsonArray.getJSONObject(j);
                            sliders.add(new Slider(jsonObject1));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sliders;
    }

    private ArrayList<Carousel> parseCarousels(Object object) {
        ArrayList<Carousel> carousels = new ArrayList<>();
        try {
            JSONArray carouselsJsonArray = null;
            if (object instanceof JSONObject) {
                JSONObject json = (JSONObject) object;
                carouselsJsonArray = json.getJSONArray("carousels");
            } else carouselsJsonArray = (JSONArray) object;
            if (carouselsJsonArray != null)
                for (int i = 0; i < carouselsJsonArray.length(); i++) {
                    JSONObject jsonObject = carouselsJsonArray.getJSONObject(i);
                    carousels.add(new Carousel(jsonObject));
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carousels;
    }
}
