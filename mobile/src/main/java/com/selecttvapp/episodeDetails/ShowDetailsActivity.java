package com.selecttvapp.episodeDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.Episode;
import com.selecttvapp.ui.fragments.ShowEpisodeInfoFragment;
import com.selecttvapp.ui.views.NonSwipeableViewPager;

import java.util.ArrayList;

public class ShowDetailsActivity extends FragmentActivity implements ShowContentFragment.ShowIntractionListener {
    public static final String TAB_EPISODES = "Episodes";
    public static final String TAB_SHOW_INFO = "Show Info";

    private FontHelper fontHelper = new FontHelper();
    private Episode show;

    private NonSwipeableViewPager mViewPager;
    private SmartTabLayout smartTabLayout;
    private ImageButton backButton;
    private ImageView favoriteIcon;

    private int showid = 0;
    private int paymode = -1;
    public static boolean[] isActiveTab;
    private boolean isFavoriteShow = false;

    public ImageView getFavoriteIcon() {
        return favoriteIcon;
    }

    public static Intent getIntent(Activity activity, int showid, int paymode) {
        Intent intent = new Intent(activity, ShowDetailsActivity.class);
        intent.putExtra("showid", showid);
        intent.putExtra("paymode", paymode);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details1);
        isActiveTab = null;
        loadViewData(true);
    }

    @Override
    protected void onResume() {
        Log.d("showdetails", "onResume: ");
        super.onResume();
        Utilities.googleAnalytics(Constants.TV_SHOWS);
    }

    private void loadViewData(boolean load) {
        ArrayList<String> tabMenus = new ArrayList<>();
        tabMenus.add(TAB_EPISODES);
        tabMenus.add(TAB_SHOW_INFO);

        favoriteIcon = (ImageView) findViewById(R.id.favoriteIcon);
        backButton = (ImageButton) findViewById(R.id.backButton);

        //setting up d-pad
        backButton.requestFocus();
        backButton.setFocusable(true);
        backButton.setFocusableInTouchMode(true);
        backButton.setBackground(getResources().getDrawable(R.drawable.btn_selector_white));
        backButton.setPadding(40, 20, 40, 20);
        // backButton.setNextFocusRightId(R.id.pager_item_tv_shows_screen2_pager);
        backButton.setNextFocusDownId(R.id.switchImage1);

        favoriteIcon.setFocusable(true);
        favoriteIcon.setFocusableInTouchMode(true);
        favoriteIcon.setBackground(getResources().getDrawable(R.drawable.btn_selector_white));
        favoriteIcon.setPadding(30, 30, 30, 30);
        // favoriteIcon.setNextFocusLeftId(R.id.pager_item_tv_shows_screen2_pager);
        favoriteIcon.setNextFocusDownId(R.id.episodeImage1);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        smartTabLayout = (SmartTabLayout) findViewById(R.id.maintab1);
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
        mViewPager.setPagingEnabled(false);
        mViewPager.clearOnPageChangeListeners();

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabMenus);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        smartTabLayout.setViewPager(mViewPager);
        setPageChangeListener(smartTabLayout);

        if (getIntent().hasExtra("showid"))
            showid = getIntent().getExtras().getInt("showid");
        if (getIntent().hasExtra("paymode"))
            paymode = getIntent().getExtras().getInt("paymode");

        if (paymode == -1) {
            paymode = RabbitTvApplication.getInstance().getPaymode();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void setShow(Episode show) {
        this.show = show;
    }

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {        // Tab Titles
        private ArrayList<String> list_data;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<String> list_data) {
            super(fm);
            this.list_data = list_data;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return ShowContentFragment.instance(showid, paymode);
            else if (position == 1)
                return ShowEpisodeInfoFragment.instance(showid, paymode);
            return null;
        }

        @Override
        public int getCount() {
            return list_data.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return Utilities.asUpperCaseFirstChar(list_data.get(position));
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

    public void changeTabsTitleTypeFace(LinearLayout ly, int position) {


        if (ly.getChildCount() == 2) {
            TextView t1 = (TextView) ly.getChildAt(0);
            TextView t2 = (TextView) ly.getChildAt(1);
            t1.setId(R.id.pager_item_tv_shows_screen2_pager);
            t2.setId(R.id.pager_item_tv_shows_screen2_pager2);

            t1.setFocusableInTouchMode(true);
            t1.setFocusable(true);
            t1.setBackground(getResources().getDrawable(R.drawable.btn_selector_pager_item));
            t1.setPadding(30, 10, 30, 10);
            t1.setNextFocusDownId(R.id.episodeImage1);

            t2.setFocusableInTouchMode(true);
            t2.setFocusable(true);
            t2.setBackground(getResources().getDrawable(R.drawable.btn_selector_pager_item));
            t2.setPadding(30, 10, 30, 10);
            t2.setNextFocusDownId(R.id.episodeImage1);

            if (position == 0) {
                t2.requestFocus();
                fontHelper.applyFonts(FontHelper.MYRIADPRO_SEMIBOLD, t1);
                //backButton.requestFocus();
                backButton.setNextFocusRightId(R.id.pager_item_tv_shows_screen2_pager2);
                favoriteIcon.setNextFocusLeftId(R.id.pager_item_tv_shows_screen2_pager2);

            }
            if (position == 1) {

                fontHelper.applyFonts(FontHelper.MYRIADPRO_SEMIBOLD, t2);
                backButton.requestFocus();
                backButton.setNextFocusRightId(R.id.pager_item_tv_shows_screen2_pager);
                favoriteIcon.setNextFocusLeftId(R.id.pager_item_tv_shows_screen2_pager);

                //}
            }
        } else {
            for (int j = 0; j < ly.getChildCount(); j++) {
                TextView tvTabTitle = (TextView) ly.getChildAt(j);
                // tvTabTitle.setId(R.id.pager_item_tv_shows_screen2_pager);
                fontHelper.applyFonts(FontHelper.MYRIADPRO_REGULAR, tvTabTitle);
                if (j == position)
                    fontHelper.applyFonts(FontHelper.MYRIADPRO_SEMIBOLD, tvTabTitle);
            }
        }
    }

}//end the class
