package com.selecttvapp.ui.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.selecttvapp.SlidersAndCarousels.CarouselsContentFragment;
import com.selecttvapp.callbacks.CarouselsListener;
import com.selecttvapp.model.Carousel;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 28-Sep-17.
 */

public class CarouselsSectionsPagerAdapter extends FragmentStatePagerAdapter {
    private final String PARAM_POSIOTION = "position";
    private final String PARAM_SUBMENU_ITEM_ID = "sub_menu_item_id";
    private final String PARAM_PYAMODE = "paymode";
    private final String PARAM_LIST = "array";

    private ArrayList<Carousel> horizontalListdata = new ArrayList<>();
    private CarouselsListener callback;
    private int payMode;

    public CarouselsSectionsPagerAdapter(FragmentManager fm, int payMode, ArrayList<Carousel> horizontalListdata, CarouselsListener callback) {
        super(fm);
        this.payMode = payMode;
        this.horizontalListdata.clear();
        this.horizontalListdata = horizontalListdata;
        this.callback = callback;
    }

    @Override
    public Fragment getItem(int position) {
        CarouselsContentFragment fragment = new CarouselsContentFragment(callback);
        Bundle args = new Bundle();
        args.putInt(PARAM_POSIOTION, position);
        args.putInt(PARAM_SUBMENU_ITEM_ID, horizontalListdata.get(position).getId());
        args.putInt(PARAM_PYAMODE, payMode);
        args.putSerializable(PARAM_LIST, horizontalListdata.get(position).getData_list());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return horizontalListdata.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return horizontalListdata.get(position).getName();
    }
}
