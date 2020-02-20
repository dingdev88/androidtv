package com.selecttvapp.presentation.fragments;

import android.app.Activity;
import android.os.Handler;

import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.Slider;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.presentation.views.ViewMytoolPPV;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.fragments.MytoolsPPVFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 22-Nov-17.
 */

public class PresenterMytoolPPVFragment {
    private Activity activity;
    private ProgressHUD mProgressHUD;
    private ViewMytoolPPV mListener;
    private Handler handler=new Handler();

    private String KEY = "my-tools-ppv";

    public PresenterMytoolPPVFragment(MytoolsPPVFragment activity) {
        this.activity = activity.getActivity();
        mListener = activity;
    }

    public void loadMytoolPPV() {
        if (RabbitTvApplication.getInstance().getSliderBeanHashMap().containsKey(KEY) || RabbitTvApplication.getInstance().getHorizontalBeanHashMap().containsKey(KEY)) {
            ArrayList<Slider> sliders = RabbitTvApplication.getInstance().getSliderBeanHashMap().get(KEY);
            ArrayList<Carousel> carousels = RabbitTvApplication.getInstance().getHorizontalBeanHashMap().get(KEY);
            mListener.loadContent(sliders, carousels);
            return;
        }
        startProgressDialog();
        Thread thread = new Thread(() -> {
            try {
                JSONObject response = JSONRPCAPI.loadMyToolsPPV();
                if (response == null) return;

                final ArrayList<Slider> sliders = parseSliders(response);
                final ArrayList<Carousel> carousels = parseCarousels(response);
                if (sliders.size() > 0)
                    RabbitTvApplication.getInstance().getSliderBeanHashMap().put(KEY, sliders);
                if (carousels.size() > 0)
                    RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put(KEY, carousels);

                handler.post(() -> mListener.loadContent(sliders, carousels));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stopProgressDialog();
            }
        });
        thread.start();
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

    private void startProgressDialog() {
        startProgressDialog(true, true);
    }

    private void startProgressDialog(boolean indeterminate, boolean cancelable) {
        mProgressHUD = ProgressHUD.show(activity, "Please wait..", indeterminate, cancelable, null);
    }

    private void stopProgressDialog() {
        handler.post(() -> {
            if (mProgressHUD.isShowing())
                mProgressHUD.dismiss();
        });
    }
}
