package com.selecttvapp.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.SlidersAndCarousels.SlidersAndCarouselsFragment;
import com.selecttvapp.common.Constants;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.Slider;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.prefrence.AppPrefrence;
import com.selecttvapp.ui.dialogs.ProgressHUD;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 28-Sep-17.
 */

public class OnDemandSuggestionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String KEY = "ondemand_suggestions";
    public static final String METHOD = "pages.get_front_page";
    // TODO: Rename and change types of parameters
    private String mParam1 = "";
    private String mParam2 = "";
    private Handler handler = new Handler();

    private ArrayList<Carousel> carousels = new ArrayList<>();
    private ArrayList<Slider> sliders = new ArrayList<>();

    private int payMode = Constants.ALL;

    public OnDemandSuggestionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OnDemandSuggestionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OnDemandSuggestionFragment newInstance(String param1, String param2) {
        OnDemandSuggestionFragment fragment = new OnDemandSuggestionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private int fragmentContainerId() {
        return R.id.layoutContent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ondemand_suggestions, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        payMode = RabbitTvApplication.getInstance().getPaymode();
        if (!mParam1.isEmpty())
            loadSuggestions();
    }

    private void loadContent(ArrayList<Slider> sliders, ArrayList<Carousel> carousels) {
        if (sliders == null && carousels == null)
            return;
        try {
            Fragment fragment = SlidersAndCarouselsFragment.newInstance(0, payMode, Constants.VIEW_ONDEMAND_SUGGESTIONS, sliders, carousels);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(fragmentContainerId(), fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //dev7 api
    private void loadSuggestions() {
        if (RabbitTvApplication.getInstance().getSliderBeanHashMap().containsKey(KEY) || RabbitTvApplication.getInstance().getHorizontalBeanHashMap().containsKey(KEY)) {
            sliders = RabbitTvApplication.getInstance().getSliderBeanHashMap().get(KEY);
            carousels = RabbitTvApplication.getInstance().getHorizontalBeanHashMap().get(KEY);
            loadContent(sliders, carousels);
        } else {
            final ProgressHUD progressHUD = ProgressHUD.show(getActivity(), "Please wait..", true, false, null);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    if (progressHUD.isShowing())
                        progressHUD.dismiss();
                    try {
                        String defaultFrontPage = (AppPrefrence.getInstance().getDefaultFrontPage());
                        final JSONObject json = JSONRPCAPI.getOndemandSuggestions(defaultFrontPage); //updated on 22-02-2019
                        // final JSONObject json = JSONRPCAPI.getOndemandSuggestions("home");
                        if (json != null) {
                            handler.post(() -> {
                                sliders = parseSliders(json);
                                carousels = parseCarousels(json);
                                if (sliders.size() > 0)
                                    RabbitTvApplication.getInstance().getSliderBeanHashMap().put(KEY, sliders);
                                if (carousels.size() > 0)
                                    RabbitTvApplication.getInstance().getHorizontalBeanHashMap().put(KEY, carousels);
                                loadContent(sliders, carousels);
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
