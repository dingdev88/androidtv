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
import com.selecttvapp.presentation.fragments.PresenterMytoolPPVFragment;
import com.selecttvapp.presentation.views.ViewMytoolPPV;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MytoolsPPVFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MytoolsPPVFragment extends FragmentHome implements ViewMytoolPPV {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_PAYMODE = "paymode";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private PresenterMytoolPPVFragment presenter;

    private FrameLayout layoutContent;

    private int payMode = Constants.PAID;

    public MytoolsPPVFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param payMode Parameter 1.
     * @param param2  Parameter 2.
     * @return A new instance of fragment MytoolsPPVFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MytoolsPPVFragment newInstance(int payMode, String param2) {
        MytoolsPPVFragment fragment = new MytoolsPPVFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_PAYMODE, payMode);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PresenterMytoolPPVFragment(this);
        if (getArguments() != null) {
//            payMode = getArguments().getInt(ARG_PARAM_PAYMODE);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_mytools_ppv;
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
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.PAY_PER_VIEW_SCREEN);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (presenter != null)
            presenter.loadMytoolPPV();
    }

    @Override
    public void loadContent(ArrayList<Slider> sliders, ArrayList<Carousel> carousels) {
        if (sliders == null && carousels == null)
            return;
        try {
            Fragment fragment = SlidersAndCarouselsFragment.newInstance(0, payMode, Constants.VIEW_MYTOOLS_PPV, sliders, carousels);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(fragmentContainerId(), fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
