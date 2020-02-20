package com.selecttvapp.channels;


import android.support.v4.app.FragmentManager;

/**
 * Created by babin on 7/10/2017.
 */

public interface TimelineListListener {
    public void onTimelineListSelected(int adapterPosition, String channelSchedulerSlug);
    public void onFragmentQueued(FragmentManager mFragmentManager,int layoutId,ChannelScheduler mChannelScheduler,int pos,ScrollingFragment mScrollingFragment);
}
