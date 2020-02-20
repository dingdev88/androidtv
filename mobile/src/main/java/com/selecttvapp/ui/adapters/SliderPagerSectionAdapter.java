package com.selecttvapp.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.selecttvapp.model.Slider;
import com.selecttvapp.ui.fragments.FragmentViewPagerItem;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 28-Sep-17.
 */

public class SliderPagerSectionAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Slider> listItems =new ArrayList<>();
    private int payMode;

    public SliderPagerSectionAdapter(FragmentManager fm, int payMode, ArrayList<Slider> listItems) {
        super(fm);
        this.payMode=payMode;
        this.listItems = listItems;
    }

    @Override
    public Fragment getItem(int pos) {
        return FragmentViewPagerItem.newInstance(pos, payMode, listItems.get(pos));
    }

    @Override
    public int getCount() {
        return listItems.size();
    }
}
