package com.selecttvapp.ui.helper;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.R;
import com.selecttvapp.common.FontHelper;
import com.selecttvapp.ui.activities.HomeActivity;

/**
 * Created by ocspl-72 on 3/1/18.
 */

public class CustomPageChangeListener implements ViewPager.OnPageChangeListener {
    private LinearLayout lyTabs;
    private FontHelper fontHelper = new FontHelper();
    private SmartTabLayout smartTabLayout;
    private IPageChangeListener pageChangeListener = null;
    private Activity activity;
    private View toolbarSearch,toolbarAppManager,toolbarMyprofile;

    public CustomPageChangeListener(Activity activity) {
        this.activity = activity;
    }

    public void setSmartTabLayout(SmartTabLayout smartTabLayout) {
        this.smartTabLayout = smartTabLayout;
        smartTabLayout.setOnPageChangeListener(this);
        lyTabs = (LinearLayout) smartTabLayout.getChildAt(0);
        changeTabsTitleTypeFace(lyTabs, 0);

    }

    public void setPageChangeListener(IPageChangeListener pageChangeListener) {
        this.pageChangeListener = pageChangeListener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (pageChangeListener != null)
            pageChangeListener.onPageChanged(position);
        if (lyTabs != null)
            changeTabsTitleTypeFace(lyTabs, position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void changeTabsTitleTypeFace(LinearLayout ly, int position) {
        for (int j = 0; j < ly.getChildCount(); j++) {
            TextView tvTabTitle = (TextView) ly.getChildAt(j);
            fontHelper.applyFont(fontHelper.tfMyriadProRegular, tvTabTitle);
            if (j == position) fontHelper.applyFont(fontHelper.tfMyriadProSemibold, tvTabTitle);
            tvTabTitle.setId(R.id.pager_item_ondemand_suggestions);
            tvTabTitle.setFocusable(true);
            tvTabTitle.setFocusableInTouchMode(true);
            tvTabTitle.setNextFocusUpId(R.id.activity_homescreen_toolbar_search);
            tvTabTitle.setNextFocusDownId(R.id.slide_prev);
            toolbarSearch=((HomeActivity)activity).getFocusOnToolbarSearch();
            toolbarMyprofile=((HomeActivity)activity).getFocusOnToolbarMyAccount();
            toolbarAppManager=((HomeActivity)activity).getFocusOnToolbarAppManager();
            toolbarSearch.setNextFocusDownId(R.id.pager_item_ondemand_suggestions);
            toolbarAppManager.setNextFocusDownId(R.id.pager_item_ondemand_suggestions);
            toolbarMyprofile.setNextFocusDownId(R.id.pager_item_ondemand_suggestions);
            tvTabTitle.setBackground(activity.getResources().getDrawable(R.drawable.btn_selector_pager_white));
            tvTabTitle.setPadding(20,5,20,20);

        }
    }

    public interface IPageChangeListener {
        void onPageChanged(int position);
    }
}
