package com.selecttvapp.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.SlidersAndCarousels.SlidersAndCarouselsFragment;
import com.selecttvapp.common.Constants;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.Slider;
import com.selecttvapp.presentation.fragments.PresenterMytoolSubscriptionsOffers;
import com.selecttvapp.presentation.views.ViewSubscriptionsOffersListener;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 28-Sep-17.
 */

public class MyToolsSubcriptionsOffersFragment extends FragmentHome implements ViewSubscriptionsOffersListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1 = "";
    private String mParam2 = "";

    private PresenterMytoolSubscriptionsOffers presenter;

    private FrameLayout layoutContent;

    private int payMode = Constants.ALL;

    public MyToolsSubcriptionsOffersFragment() {
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
    public static MyToolsSubcriptionsOffersFragment newInstance(String param1, String param2) {
        MyToolsSubcriptionsOffersFragment fragment = new MyToolsSubcriptionsOffersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = new PresenterMytoolSubscriptionsOffers(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_ondemand_suggestions;
    }

    private int fragmentContainerId(){
        return R.id.layoutContent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(getLayoutResId(), container, false);
        layoutContent = (FrameLayout) view.findViewById(R.id.layoutContent);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        payMode = RabbitTvApplication.getInstance().getPaymode();
        if (presenter != null)
            presenter.loadSubscriptionsOffers();
    }

    @Override
    public void loadContent(ArrayList<Slider> sliders, ArrayList<Carousel> carousels) {
        if (sliders == null && carousels == null)
            return;
        try {
            Fragment fragment = SlidersAndCarouselsFragment.newInstance(0, payMode, Constants.VIEW_MYTOOLS_SUBSCRIPTIONS_OFFERS, sliders, carousels);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(fragmentContainerId(), fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
