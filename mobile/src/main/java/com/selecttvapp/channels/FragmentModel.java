package com.selecttvapp.channels;

import android.support.v4.app.FragmentManager;

import java.io.Serializable;

/**
 * Created by babin on 8/22/2017.
 */

public class FragmentModel implements Serializable {
    FragmentManager mFragmentManager;
    int layoutId;

    public ScrollingFragment getmScrollingFragment() {
        return mScrollingFragment;
    }

    public void setmScrollingFragment(ScrollingFragment mScrollingFragment) {
        this.mScrollingFragment = mScrollingFragment;
    }

    ScrollingFragment mScrollingFragment;

    public FragmentModel(FragmentManager mFragmentManager, int layoutId, ChannelScheduler mChannelScheduler, int pos,ScrollingFragment mScrollingFragment) {
        this.mFragmentManager = mFragmentManager;
        this.layoutId = layoutId;
        this.mChannelScheduler = mChannelScheduler;
        this.pos = pos;
        this.mScrollingFragment=mScrollingFragment;
    }

    public FragmentManager getmFragmentManager() {
        return mFragmentManager;
    }

    public void setmFragmentManager(FragmentManager mFragmentManager) {
        this.mFragmentManager = mFragmentManager;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public ChannelScheduler getmChannelScheduler() {
        return mChannelScheduler;
    }

    public void setmChannelScheduler(ChannelScheduler mChannelScheduler) {
        this.mChannelScheduler = mChannelScheduler;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    ChannelScheduler mChannelScheduler;
    int pos;
}
