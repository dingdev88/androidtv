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
import com.selecttvapp.SlidersAndCarousels.SlidersAndCarouselsFragment;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.Slider;
import com.selecttvapp.presentation.fragments.PresenterKidsFragment;
import com.selecttvapp.presentation.views.ViewKids;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KidsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KidsFragment extends FragmentHome implements ViewKids {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_SLUG = "slug";

    // TODO: Rename and change types of parameters
    private String mPageSlug = "";
    private String mParam2 = "";

    private PresenterKidsFragment presenter;
    private FrameLayout layoutContent;

    private ArrayList<Carousel> carousels = new ArrayList<>();
    private ArrayList<Slider> sliders = new ArrayList<>();
    private int payMode = Constants.ALL;


    public KidsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param slug Parameter 1.
     * @return A new instance of fragment KidsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KidsFragment newInstance(String slug) {
        KidsFragment fragment = new KidsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_SLUG, slug);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_kids;
    }

    private int fragmentContainerId(){
        return R.id.layoutContent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = new PresenterKidsFragment(this);
        if (getArguments() != null) {
            mPageSlug = getArguments().getString(ARG_PARAM_SLUG);
        }
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

//        payMode = RabbitTvApplication.getInstance().getPaymode();
        if (mPageSlug.equalsIgnoreCase("pay-per-view-kids")) {
            payMode = Constants.PAID;
        }

        if (!mPageSlug.isEmpty())
            if (presenter != null)
                presenter.loadKids(mPageSlug);
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.ON_DEMANT_KIDS_SCREEN);
    }

    @Override
    public void loadContent(ArrayList<Slider> sliders, ArrayList<Carousel> carousels) {
        if (sliders == null && carousels == null)
            return;
        try {
            Fragment fragment = SlidersAndCarouselsFragment.newInstance(0, payMode, Constants.VIEW_KIDS, sliders, carousels);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(fragmentContainerId(), fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
