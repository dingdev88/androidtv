package com.selecttvapp.SlidersAndCarousels;


import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.model.Slider;
import com.selecttvapp.ui.activities.HomeActivity;
import com.selecttvapp.ui.adapters.SliderPagerSectionAdapter;
import com.selecttvapp.ui.fragments.SubscriptionsFragment;
import com.selecttvapp.ui.helper.ViewPagerSpeedScroller;
import com.selecttvapp.ui.views.AutoScrollViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Timer;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SlidersPagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SlidersPagerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM_POSITION = "position";
    private static final String ARG_PARAM_PAYMODE = "paymode";
    private static final String ARG_PARAM_SLIDERS = "sliders";
    private View toolbarSearch, toolbarMyAccount, toolbarAppManager;
    private boolean isCurrentFragmentSubscriptions=false;

    // TODO: Rename and change types of parameters
    private String mParam1 = "";
    private String mParam2 = "";
    private int mParamPosition = 0;
//    private int mParamPayMode = 0;

    private Handler handler;
    private Timer timer;
    private FrameLayout sliderCarouselLayout;
    private LinearLayout sliderDotsLayout;
    private AutoScrollViewPager sliderPager;
    private ImageView slidePrev, slideNext;

    private ArrayList<Slider> sliders = new ArrayList<>();
    private ArrayList<ImageView> dots = new ArrayList<>();

    private int payMode = RabbitTvApplication.getInstance().getPaymode();
    private int currentPagePosition = 0;


    public SlidersPagerFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public SlidersPagerFragment(ArrayList<Slider> sliders) {
        this.sliders = sliders;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param payMode Parameter 2.
     * @return A new instance of fragment OnDemandSubscriptionsContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SlidersPagerFragment newInstance(int pagePosition, int payMode, ArrayList<Slider> sliders) {
        SlidersPagerFragment fragment = new SlidersPagerFragment();
        if (sliders.size() > 0)
            fragment.sliders.addAll(sliders);
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_POSITION, pagePosition);
        args.putInt(ARG_PARAM_PAYMODE, payMode);
        args.putSerializable(ARG_PARAM_SLIDERS, sliders);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamPosition = getArguments().getInt(ARG_PARAM_POSITION);
            payMode = getArguments().getInt(ARG_PARAM_PAYMODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sliders_pager, container, false);
        slidePrev = view.findViewById(R.id.slide_prev);
        slideNext = view.findViewById(R.id.slide_next);
        sliderCarouselLayout = (FrameLayout) view.findViewById(R.id.sliderCarouselLayout);
        sliderDotsLayout = (LinearLayout) view.findViewById(R.id.slider_dots_layout);
        sliderPager = (AutoScrollViewPager) view.findViewById(R.id.slider_pager);
        sliderPager.setInterval(4000);
        sliderPager.setCycle(true);
        sliderPager.setScrollDuration(1000);
        sliderPager.setDirection(AutoScrollViewPager.RIGHT);
        sliderPager.setStopScrollWhenTouch(true);
        stopAutoScroll(); // added to stop the auto scroll 23-05-2019

        toolbarSearch = ((HomeActivity) getActivity()).getFocusOnToolbarSearch();
        toolbarAppManager = ((HomeActivity) getActivity()).getFocusOnToolbarAppManager();
        toolbarMyAccount = ((HomeActivity) getActivity()).getFocusOnToolbarMyAccount();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (sliders != null)
            setSlider(sliders);

        Fragment currentFrag=getParentFragment();
        if(currentFrag instanceof SubscriptionsFragment){
            isCurrentFragmentSubscriptions=true;
            toolbarSearch.requestFocus();
        }else {
        toolbarSearch.requestFocus();
        toolbarSearch.setNextFocusDownId(R.id.slide_prev);
        toolbarMyAccount.setNextFocusDownId(R.id.slide_prev);
        toolbarAppManager.setNextFocusDownId(R.id.slide_prev);
        }


        slidePrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCurrentFragmentSubscriptions){
                    sliderPager.setCurrentItem(sliderPager.getCurrentItem() - 1);
                    slidePrev.setFocusable(true);
                    slidePrev.setFocusableInTouchMode(true);
                    slidePrev.requestFocus();
                    slidePrev.setNextFocusDownId(R.id.btnWatchNow);
                }else {
                    sliderPager.setCurrentItem(sliderPager.getCurrentItem() - 1);
                    slidePrev.setFocusable(true);
                    slidePrev.setFocusableInTouchMode(true);
                    slidePrev.requestFocus();
                    slidePrev.setNextFocusDownId(R.id.btnWatchNow);
                }
            }
        });
        slideNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCurrentFragmentSubscriptions){
                    sliderPager.setCurrentItem(sliderPager.getCurrentItem() + 1);
                    slideNext.setFocusable(true);
                    slideNext.setFocusableInTouchMode(true);
                    slideNext.requestFocus();
                    slideNext.setNextFocusDownId(R.id.btnWatchNow);
                    slideNext.setNextFocusUpId(R.id.pager_item_ondemand_suggestions);
                }else {
                    sliderPager.setCurrentItem(sliderPager.getCurrentItem() + 1);
                    slideNext.setFocusable(true);
                    slideNext.setFocusableInTouchMode(true);
                    slideNext.requestFocus();
                    slideNext.setNextFocusDownId(R.id.btnWatchNow);
                    slideNext.setNextFocusUpId(R.id.activity_homescreen_toolbar_search);
                }

            }
        });
    }

    @Override
    public void onDestroyView() {
        if (sliders != null)
            sliders.clear();
        removeCallbacks();
        stopAutoScroll();
        if (sliderCarouselLayout != null)
            sliderCarouselLayout.setVisibility(View.GONE);
        super.onDestroyView();
    }

    private void setSlider(ArrayList<Slider> sliders) {
        try {
            if (sliders != null)
                if (sliders.size() > 0) {
                    if (sliders.size() > 10) {
                        sliders.addAll(sliders.subList(0, 10));
                    }
                    sliderCarouselLayout.setVisibility(View.VISIBLE);
                    dots.clear();
                    sliderDotsLayout.removeAllViews();
                    SliderPagerSectionAdapter fragmentPagerItemAdapter = new SliderPagerSectionAdapter
                            (getChildFragmentManager(), payMode, sliders);
                    sliderPager.setAdapter(fragmentPagerItemAdapter);
                    sliderPager.setCurrentItem(0);
                    addDots(sliders.size());
                    selectDot(0);
                   /* if (sliders.size() > 0)   // modified to stop the auto scroll 23-05-2019
                        startAutoScroll();*/
                } else sliderCarouselLayout.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startAutoScroll() {
        if (sliderPager != null)
            sliderPager.startAutoScroll();
    }

    private void stopAutoScroll() {
        if (sliderPager != null)
            sliderPager.stopAutoScroll();
    }

//    private void setAutoScrollSliderPager(final int size) {
//        try {
//            removeCallbacks();
//            handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        getActivity().getWindow().getDecorView().clearFocus();
//                        if (sliderPager.getCurrentItem() == (size - 1))
//                            sliderPager.setCurrentItem(0);
//                        else
//                            sliderPager.setCurrentItem(sliderPager.getCurrentItem() + 1, true);
//                        if (handler != null)
//                            handler.postDelayed(this, 4000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 5000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public void removeCallbacks() {
        if (handler != null) {
            handler.removeCallbacks(null);
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    public void selectDot(int idx) {
        Resources res = getResources();
        for (int i = 0; i < dots.size(); i++) {
            int drawableId = (i == idx) ? (R.drawable.dot_active) : (R.drawable.dot_inactvie);
            Drawable drawable = res.getDrawable(drawableId);
            assert drawable != null;
            drawable.setBounds(0, 0, 0, 0);
            if (i == idx) {
                dots.get(i).setFocusableInTouchMode(true);
                dots.get(i).setFocusable(true);
                dots.get(i).requestFocus();
            }
            dots.get(i).setImageDrawable(drawable);

        }
    }

    public void addDots(int sliderPages) {
        for (int i = 0; i < sliderPages; i++) {
            ImageView dot = new ImageView(getActivity());
            dot.setImageDrawable(getResources().getDrawable(R.drawable.dot_active));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 2, 10, 2);
            sliderDotsLayout.addView(dot, params);
            dots.add(dot);
        }

        sliderPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setScrollSpeed(ViewPager viewpager) {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            ViewPagerSpeedScroller scroller = new ViewPagerSpeedScroller(viewpager.getContext());
            scroller.setDuration(1000);
            mScroller.set(viewpager, scroller);
        } catch (Exception e) {
        }
    }
}
