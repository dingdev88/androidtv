package com.selecttvapp.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.selecttvapp.R;
import com.selecttvapp.ui.fragments.TestFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class TestFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
    //protected static final String[] CONTENT = new String[] { "This", "Is", "A", "Test", };

    protected static final String[] TITLE = new String[] {"Welcome to Arvig", "Hundreds of 24/7 Channels", "Rethink the Way You Watch TV"};
    protected static final String[] DESCRIPTION = new String[] {"The World's Largest Library of FREE Online Entertainment", "TV, Movies, Music, News, Sports and so much more...", ""};

    protected static final int[] ICONS = new int[] {
            R.drawable.icon_logo,
            R.drawable.icon_logo,
            R.drawable.icon_logo
    };



    private int mCount = TITLE.length;

    public TestFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return TestFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = TestFragmentAdapter.TITLE[position % TITLE.length];
        return title;
    }

    @Override
    public int getIconResId(int index) {
      return ICONS[index % ICONS.length];
    }

    public void setCount(int count) {
        if (count > 0 && count <= 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}