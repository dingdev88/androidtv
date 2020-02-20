package com.selecttvapp.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.selecttvapp.R;
import com.selecttvapp.common.GlideApp;
import com.selecttvapp.common.MyGlideApp;
import com.selecttvapp.episodeDetails.ShowDetailsActivity;
import com.selecttvapp.model.Slider;
import com.selecttvapp.network.JSONRPCAPI;
import com.selecttvapp.ui.WebView;
import com.selecttvapp.ui.activities.MovieDetailsActivity;
import com.selecttvapp.ui.dialogs.ProgressHUD;
import com.selecttvapp.ui.views.DynamicImageView;
import com.selecttvapp.ui.views.ImageLoader;

import org.json.JSONObject;


public class FragmentViewPagerItem extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_POSITION = "slider_posion";
    private static final String PARAM_PAY_MODE = "paymode";
    private static final String PARAM_SLIDER = "slider";

    private Slider slider;

    private TextView sliderName;
    private TextView sliderSubName;
    private TextView sliderDescription;
    private TextView btnWatchNow;
    private DynamicImageView sliderImage;
    private ImageLoader imageLoader;
    private int sliderPosition;
    private int payMode;
    private AQuery aq;

    // TODO: Rename and change types and number of parameters
    public static FragmentViewPagerItem newInstance(int sliderPosition, int payMode, Slider slider) {
        FragmentViewPagerItem fragment = new FragmentViewPagerItem();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_POSITION, sliderPosition);
        args.putInt(PARAM_PAY_MODE, payMode);
        args.putSerializable(PARAM_SLIDER, slider);
        fragment.setArguments(args);

        return fragment;
    }

    public FragmentViewPagerItem() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sliderPosition = getArguments().getInt(ARG_PARAM_POSITION);
            payMode = getArguments().getInt(PARAM_PAY_MODE);
            slider = (Slider) getArguments().getSerializable(PARAM_SLIDER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_viewpager_item, container, false);
        aq = new AQuery(getActivity());

        try {
            sliderSubName = (TextView) v.findViewById(R.id.sliderSubName);
            sliderName = (TextView) v.findViewById(R.id.sliderName);
            sliderDescription = (TextView) v.findViewById(R.id.sliderDescription);
            btnWatchNow = (TextView) v.findViewById(R.id.btnWatchNow);
            sliderImage = (DynamicImageView) v.findViewById(R.id.sliderImage);

            sliderName.setText(slider.getName());
            sliderSubName.setText(slider.getTitle());
            sliderDescription.setText(slider.getDescription());
            try {


              //  aq.id(sliderImage).image(slider.getImage());
                GlideApp.with(getActivity())
                        .load(slider.getImage())// resizes the image to 100x200 pixels but does not respect aspect ratio
                        .into(sliderImage);

            } catch (Exception e) {
                e.printStackTrace();
            }

            btnWatchNow.setOnClickListener(v1 -> {
                if (!btnWatchNow.getText().equals("PLAY NOW"))
                    loadSliderEntity(slider.getId(), slider.getType(),slider.getUrl(),slider.getName());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void loadSliderEntity(final int sliderId, final String sliderType, final String url, final String name) {
        final ProgressHUD mProgressHUD = ProgressHUD.show(getActivity(), "Please Wait...", true, false, null);
        Thread thread = new Thread(() -> {
            try {
                 if(sliderType.equalsIgnoreCase("other")){

                     Intent intent = new Intent(getActivity(),WebView.class);
                     intent.putExtra("url", url);
                     intent.putExtra("name", name);
                     startActivity(intent);
                }else {
                     JSONObject response;
                     if (sliderType.equalsIgnoreCase("m") || sliderType.equalsIgnoreCase("movie") || sliderType.equalsIgnoreCase("movie_info")) {
                         response = JSONRPCAPI.getMovieSlideEntity(sliderId);
                     } else response = JSONRPCAPI.getSlideEntity(sliderId);
                     if (response != null) {
                         int id = 0;
                         String type = "";

                         if (response.has("entity_id"))
                             id = response.getInt("entity_id");
                         if (response.has("type"))
                             type = response.getString("type");


                         if (type.equalsIgnoreCase("m") || type.equalsIgnoreCase("movie")) {
                             Intent intent = MovieDetailsActivity.getIntent(getActivity(), id);
                             startActivity(intent);
                         } else {
                             Intent intent = ShowDetailsActivity.getIntent(getActivity(), id, payMode);
                             startActivity(intent);
                         }
                     }
                 }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mProgressHUD.isShowing())
                    mProgressHUD.dismiss();
            }
        });
        thread.start();
    }
}



