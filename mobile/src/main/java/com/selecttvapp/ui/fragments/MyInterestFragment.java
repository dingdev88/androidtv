package com.selecttvapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.selecttvapp.R;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.FavoriteBean;
import com.selecttvapp.model.GridSpacingItemDecoration;
import com.selecttvapp.presentation.fragments.PresenterMyInterest;
import com.selecttvapp.presentation.views.ViewMyInterestListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * to handle interaction events.
 * Use the {@link MyInterestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyInterestFragment extends Fragment implements ViewMyInterestListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAB_TV_SHOWS = "TV Shows";
    private static final String TAB_MOVIES = "Movies";
    private static final String TAB_MOVIE_GENRES = "Movie Genres";
    private static final String TAB_CHANNELS = "Channels";
    private static final String TAB_TV_NETWORKS = "TV Networks";
    private static final String TAB_VIDEO_LBRARIES = "Video libraries";
    private final String ARG_TYPE = "type";
    // TODO: Rename and change types of parameters
    private String mParamType = "";

    private PresenterMyInterest presenter;

    private RecyclerView interest_recycler_view, interestshow_recycler_view;
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;
    private LinearLayout mMyInterestLayout;

    public MyInterestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyInterestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyInterestFragment newInstance(String param1, String param2) {
        MyInterestFragment fragment = new MyInterestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PresenterMyInterest(this);

        if (getArguments() != null) {
            mParamType = getArguments().getString(ARG_PARAM1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_interest, container, false);

        mMyInterestLayout = (LinearLayout) view.findViewById(R.id.myinterestlayout);
        smartTabLayout = (SmartTabLayout) view.findViewById(R.id.smartTabLayout);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        interest_recycler_view = (RecyclerView) view.findViewById(R.id.interest_recycler_view);
        interestshow_recycler_view = (RecyclerView) view.findViewById(R.id.interestshow_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        interest_recycler_view.setLayoutManager(linearLayoutManager);

        GridLayoutManager grid_linear_layoutManager = new GridLayoutManager(getActivity(), 4);
        grid_linear_layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        interestshow_recycler_view.hasFixedSize();
        interestshow_recycler_view.setLayoutManager(grid_linear_layoutManager);
        int spanCount = 4; // 3 columns
        int spacing = 25; // 50px
        boolean includeEdge = true;
        interestshow_recycler_view.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        if (presenter != null)
            presenter.loadFavoriteList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.MY_INTERESTS_SCREEN);
    }

    @Override
    public void onDestroyView() {
        PresenterMyInterest.dismisDialog();
        super.onDestroyView();
    }

    @Override
    public void loadFavoriteList(HashMap<String, ArrayList<FavoriteBean>> favoriteList) {
        if (getActivity() == null || getActivity().isFinishing() || getChildFragmentManager() == null || !isAdded())
            return;

        mMyInterestLayout.setVisibility(View.VISIBLE);
        viewPager.setOffscreenPageLimit(presenter.getTabs().size());
        PresenterMyInterest.SectionsPagerAdapter sectionsPagerAdapter = presenter.getSectionsPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        smartTabLayout.setViewPager(viewPager);
        presenter.setPageChangeListener(smartTabLayout);

        if (!mParamType.isEmpty()) {
            if (mParamType.equalsIgnoreCase("shows") || mParamType.equalsIgnoreCase("TV Shows")) {
                viewPager.setCurrentItem(0, true);
            } else if (mParamType.equalsIgnoreCase("movies")) {
                viewPager.setCurrentItem(1, true);
            } else if (mParamType.equalsIgnoreCase("movie genres") || mParamType.equalsIgnoreCase("moviegenre")) {
                viewPager.setCurrentItem(2, true);
            } else if (mParamType.equalsIgnoreCase("channel") || mParamType.equalsIgnoreCase("Channels")) {
                viewPager.setCurrentItem(3, true);
            } else if (mParamType.equalsIgnoreCase("networks") || mParamType.equalsIgnoreCase("network")) {
                viewPager.setCurrentItem(4, true);
            } else if (mParamType.equalsIgnoreCase("video libraries") || mParamType.equalsIgnoreCase("videolibrary")) {
                viewPager.setCurrentItem(5, true);
            }
        }
    }

    @Override
    public void showSessionExpiredDialog() {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
