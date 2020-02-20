package com.selecttvapp.SlidersAndCarousels;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.selecttvapp.R;
import com.selecttvapp.RabbitTvApplication;
import com.selecttvapp.callbacks.CarouselsListener;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.HorizontalListitemBean;
import com.selecttvapp.ui.adapters.CarouselsAdapter;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 09-Aug-17.
 */

@SuppressLint("ValidFragment")
public class CarouselsContentFragment extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_LIST_ITEMS = "list_items";
    public static final String PARAM_SUBMENU_ITEM_ID = "sub_menu_item_id";
    public static final String PARAM_PAY_MODE = "paymode";

    private ArrayList<HorizontalListitemBean> horizontalListdata = new ArrayList<>();
    private RecyclerView recyclerView;
    private CarouselsListener carouselsListener;

    private int currentSubmenuItemId;
    private int LIMIT = 20;
    private int payMode = RabbitTvApplication.getInstance().getPaymode();

    public CarouselsContentFragment() {
    }

    @SuppressLint("ValidFragment")
    public CarouselsContentFragment(CarouselsListener carouselsListener) {
        this.carouselsListener = carouselsListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            currentSubmenuItemId = getArguments().getInt(PARAM_SUBMENU_ITEM_ID);
            payMode = getArguments().getInt(PARAM_PAY_MODE);
            horizontalListdata = (ArrayList<HorizontalListitemBean>) getArguments().getSerializable("array");
            Log.e("image_list_size", "-->" + horizontalListdata.size());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sub_menu_items, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        if (horizontalListdata.size() > 0)
            if (!horizontalListdata.get(0).getType().equalsIgnoreCase("s"))
                LIMIT++;

        ArrayList<HorizontalListitemBean> tempList = new ArrayList<>();
        if (horizontalListdata.size() <= LIMIT) {
            tempList = horizontalListdata;
        } else if (horizontalListdata.size() > LIMIT) {
            tempList.addAll(horizontalListdata.subList(0, LIMIT));
            tempList.add(null);
        }

        if (tempList.size() > 0) {
            if (tempList.get(0).getType().equalsIgnoreCase("s")) {
                GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 4);
                recyclerView.setLayoutManager(mLayoutManager);
            } else {
                GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 4);
                mLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(mLayoutManager);
            }

            CarouselsAdapter mAdapter = new CarouselsAdapter(getActivity(), currentSubmenuItemId, tempList, payMode, carouselsListener);
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager)
                mAdapter.setSpanSizeLookup((GridLayoutManager) recyclerView.getLayoutManager());
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.CAROUSELS_SCREEN);
    }
}