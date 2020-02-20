package com.selecttvapp.personalization;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.ui.views.NonSwipeableViewPager;

/**
 * Created by Appsolute dev on 02-Aug-17.
 */

public class PersonalizationDialogFragment extends DialogFragment {
    public static final String TAG = "Personalization_Fragment";

    private NonSwipeableViewPager mViewPager;
    private Button btnPrevious, btnNext;
    private TextView mTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_DialogFragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getDialog().setCancelable(true);
        getDialog().getWindow().setGravity(Gravity.CENTER);
        return inflater.inflate(R.layout.activity_personalization, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (NonSwipeableViewPager) view.findViewById(R.id.viewPager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPagingEnabled(false);
        mViewPager.addOnPageChangeListener(pageChangeListener);
        mViewPager.setAdapter(new SectionPageAdapter(getChildFragmentManager()));

        mTitle = (TextView) view.findViewById(R.id.title);
        setTitle(0);

        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnPrevious = (Button) view.findViewById(R.id.btnPrevious);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next();
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previous();
            }
        });
    }


    private class SectionPageAdapter extends FragmentStatePagerAdapter {

        public SectionPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Bundle bundle = new Bundle();
            if (i == 0)
                bundle.putString("type", "tv_shows");
            if (i == 1)
                bundle.putString("type", "movies");
            if (i == 2)
                bundle.putString("type", "radio");

            PersonalizationContentFragment fragment = new PersonalizationContentFragment();
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            setTitle(i);
            if (i == 0) {
                btnPrevious.setBackgroundColor(0xFF323232);
                btnPrevious.setTextColor(0xFF878787);
            } else {
                btnPrevious.setBackgroundColor(0xFF009dfe);
                btnPrevious.setTextColor(0xFFFFFFFF);
            }
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void setTitle(int position) {
        if (position == 0)
            mTitle.setText("Add Some TV Shows You Enjoy");
        if (position == 1)
            mTitle.setText("Now Pick Your Most Watched Movie Categories");
        if (position == 2)
            mTitle.setText("Lastly, Music Genres You Listen To");
    }

    public void next() {
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem != 2)
            mViewPager.setCurrentItem(currentItem + 1, true);
        else if (currentItem == 2) {
            dismiss();
            return;
        }

        if (mViewPager.getCurrentItem() == 0) {
            btnPrevious.setBackgroundColor(0xFF323232);
            btnPrevious.setTextColor(0xFF878787);
        } else {
            btnPrevious.setBackgroundColor(0xFF009dfe);
            btnPrevious.setTextColor(0xFFFFFFFF);
        }
    }

    public void previous() {
        int currentItem = mViewPager.getCurrentItem();
        if (currentItem != 0)
            mViewPager.setCurrentItem(currentItem - 1, true);

        if (mViewPager.getCurrentItem() == 0) {
            btnPrevious.setBackgroundColor(0xFF323232);
            btnPrevious.setTextColor(0xFF878787);
        } else {
            btnPrevious.setBackgroundColor(0xFF009dfe);
            btnPrevious.setTextColor(0xFFFFFFFF);
        }
    }
}
