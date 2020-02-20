package com.selecttvapp.ui.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.channels.HorizontalchannelSwipingView;
import com.selecttvapp.ui.views.CustomNestedScrollView;
import com.selecttvapp.ui.views.LoopHScrollView;

/**
 * Created by babin on 8/18/2017.
 */

public class LayoutBinder {
    public LinearLayout layoutCategories, HomeContainerToolbar;
    public RecyclerView categoriesListView, fragmentChannelAlllist, fragmentChannelProgramList, channelShowAlllist;
    public FrameLayout playerLayout, showsViewLayout, playerFrameLayout;
    public TextView btnLive, showTabTextview, browseTextView, txtStream, txtLive, txtVideoTitle, channelTabTextview;
    public CustomNestedScrollView overallScroll;
    public ImageView imgSTAR, imgVOLUME, imgPAUSE, imgFULLSCREEN;
    public HorizontalchannelSwipingView swipingView;
    public LinearLayout rightSliderLayout, linearSubMainRight, channelVisibleLayout;
    public View rulerLine;
    public ImageView imgLeft;
    public ImageView imgRight;
    public LoopHScrollView endlessHScrollView;
    private View mView;

    public LayoutBinder(View mView) {
        this.mView = mView;

    }

    public void initializeview() {
        layoutCategories= mView.findViewById(R.id.layoutCategories);
        categoriesListView = mView.findViewById(R.id.categories_list_view);
        HomeContainerToolbar = mView.findViewById(R.id.container_toolbar);
        fragmentChannelAlllist = mView.findViewById(R.id.fragment_channel_alllist);
        fragmentChannelProgramList = mView.findViewById(R.id.fragment_channel_program_list);
        channelShowAlllist = mView.findViewById(R.id.channel_show_alllist);
        playerLayout = mView.findViewById(R.id.player_layout);
        playerFrameLayout = mView.findViewById(R.id.player_frame_layout);
        showsViewLayout = mView.findViewById(R.id.showsView_layout);
        btnLive = mView.findViewById(R.id.btnLive);
        showTabTextview = mView.findViewById(R.id.show_tab_textview);
        channelTabTextview = mView.findViewById(R.id.channel_tab_textview);
        txtStream = mView.findViewById(R.id.txtStream);
        browseTextView = mView.findViewById(R.id.browse_textView);
        txtVideoTitle = mView.findViewById(R.id.txtVideoTitle);
        txtLive = mView.findViewById(R.id.txtLive);
        overallScroll = mView.findViewById(R.id.overall_scroll);
        imgSTAR = mView.findViewById(R.id.imgSTAR);
        imgPAUSE = mView.findViewById(R.id.imgPAUSE);
        imgVOLUME = mView.findViewById(R.id.imgVOLUME);
        imgFULLSCREEN = mView.findViewById(R.id.fullScreenIcon);
        swipingView = mView.findViewById(R.id.swipingView);
        rightSliderLayout = mView.findViewById(R.id.rightSliderLayout);
        channelVisibleLayout = mView.findViewById(R.id.channelVisibleLayout);
        linearSubMainRight = mView.findViewById(R.id.linearSubMainRight);
        rulerLine = mView.findViewById(R.id.ruler_line);
        imgLeft = mView.findViewById(R.id.imgLeft);
        imgRight = mView.findViewById(R.id.imgRight);
        endlessHScrollView = mView.findViewById(R.id.endlessHScrollView);
        channelVisibleLayout.setAlpha(1);

    }

}
