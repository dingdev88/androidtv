package com.selecttvapp.SlidersAndCarousels;


import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.network.common.AppFonts;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.CarouselsListener;
import com.selecttvapp.model.Carousel;
import com.selecttvapp.ui.adapters.CarouselsSectionsPagerAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarouselsPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarouselsPagerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM_POSITION = "position";
    private static final String ARG_PARAM_PAYMODE = "paymode";

    private Typeface MYRIADPRO_BOLD, MYRIADPRO_ITALIC, MYRIADPRO_REGULAR, MYRIADPRO_SEMIBOLD;
    // TODO: Rename and change types of parameters
    private String mParam1 = "";
    private String mParam2 = "";
    private int mParamPosition = 0;
    private int mParamPayMode = 0;

    private Handler handler;
    private ViewPager carouselsPager;
    private SmartTabLayout smartTabLayout;
    private CarouselsListener carouselsListener;

    private ArrayList<Carousel> carousels = new ArrayList<>();

    private int payMode = RabbitTvApplication.getInstance().getPaymode();
    private int currentPagePosition = 0;


    public CarouselsPagerFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public CarouselsPagerFragment(ArrayList<Carousel> carousels, CarouselsListener carouselsListener) {
        this.carousels = carousels;
        this.carouselsListener = carouselsListener;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pagePosition Parameter 1.
     * @param payMode      Parameter 2.
     * @return A new instance of fragment CarouselsPagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarouselsPagerFragment newInstance(int pagePosition, int payMode, ArrayList<Carousel> carousels, CarouselsListener carouselsListener) {
        CarouselsPagerFragment fragment = new CarouselsPagerFragment();
        if (carousels.size() > 0)
            fragment.carousels.addAll(carousels);
        fragment.carouselsListener = carouselsListener;
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_POSITION, pagePosition);
        args.putInt(ARG_PARAM_PAYMODE, payMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        MYRIADPRO_BOLD = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.MYRIADPRO_BOLD);
        MYRIADPRO_ITALIC = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.MYRIADPRO_ITALIC);
        MYRIADPRO_REGULAR = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.MYRIADPRO_REGULAR);
        MYRIADPRO_SEMIBOLD = Typeface.createFromAsset(getActivity().getAssets(), AppFonts.MYRIADPRO_SEMIBOLD);
        if (getArguments() != null) {
            mParamPosition = getArguments().getInt(ARG_PARAM_POSITION);
            payMode = getArguments().getInt(ARG_PARAM_PAYMODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_carousels_pager, container, false);
        smartTabLayout = (SmartTabLayout) view.findViewById(R.id.smartTabLayout);
        carouselsPager = (ViewPager) view.findViewById(R.id.carouselsPager);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (carousels != null)
            setCarousels(carousels);
    }

    @Override
    public void onDestroyView() {
        if (carousels != null)
            carousels.clear();
        super.onDestroyView();
    }

    private void setCarousels(ArrayList<Carousel> carousels) {
        try {
            if (carousels != null)
                if (carousels.size() > 0) {
                    for (int i = carousels.size() - 1; i >= 0; i--)
                        if (carousels.get(i).getData_list().size() <= 0)
                            carousels.remove(i);

                    if (carousels.size() <= 0)
                        return;

                    CarouselsSectionsPagerAdapter carouselsSectionsPagerAdapter = new CarouselsSectionsPagerAdapter(getChildFragmentManager(), payMode, carousels, carouselsListener);
                    carouselsPager.setAdapter(carouselsSectionsPagerAdapter);
                    smartTabLayout.setViewPager(carouselsPager);
                    setPageChangeListener(smartTabLayout);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setPageChangeListener(SmartTabLayout smartTabLayout) {
        final LinearLayout lyTabs = (LinearLayout) smartTabLayout.getChildAt(0);
        changeTabsTitleTypeFace(lyTabs, 0);
        smartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                changeTabsTitleTypeFace(lyTabs, position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void changeTabsTitleTypeFace(LinearLayout ly, int position) {
        for (int j = 0; j < ly.getChildCount(); j++) {
            TextView tvTabTitle = (TextView) ly.getChildAt(j);
            tvTabTitle.setTypeface(MYRIADPRO_REGULAR);
            if (j == position) tvTabTitle.setTypeface(MYRIADPRO_SEMIBOLD);
        }
    }

}
