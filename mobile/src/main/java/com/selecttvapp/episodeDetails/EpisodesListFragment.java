package com.selecttvapp.episodeDetails;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.SortedListHashMap.SortedListHashMap;
import com.selecttvapp.common.Constants;
import com.selecttvapp.common.Utilities;
import com.selecttvapp.model.Episode;
import com.selecttvapp.presentation.fragments.PresenterEpisodesList;
import com.selecttvapp.presentation.views.ViewShowListener;
import com.selecttvapp.ui.adapters.EpisodesAdapter;
import com.selecttvapp.ui.bean.SeasonsBean;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 28-Nov-17.
 */

public class EpisodesListFragment extends Fragment implements ViewShowListener {
    private static final String PARAM_SHOW_ID = "showid";
    private static final String PARAM_PAY_MODE = "paymode";
    private static final String PARAM_EPISODES_LIST = "episodes_list";
    private static final String PARAM_SEASON_ID = "seasonid";
    private static final String PARAM_SEASON_NUMBER = "seasonNumber";
    private static final String PARAM_POSITION = "position";

    private PresenterEpisodesList presenter;
    private Activity activity;

    private EpisodesAdapter episodeAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar progressBar;
    private TextView labelNoData;

    private int showId = 0;
    private int payMode = 0;
    private SortedListHashMap<Integer, SeasonsBean> mEpisodes;

    public EpisodesListFragment() {
    }

    public static EpisodesListFragment instance(int seasonid, int seasonNumber, int showId, int payMode, int position) {
        EpisodesListFragment fragment = new EpisodesListFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_SEASON_ID, seasonid);
        args.putInt(PARAM_SEASON_NUMBER, seasonNumber);
        args.putInt(PARAM_POSITION, position);
        args.putInt(PARAM_SHOW_ID, showId);
        args.putInt(PARAM_PAY_MODE, payMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        presenter = new PresenterEpisodesList(this);
        activity = getActivity();
        if (getArguments() != null) {
            showId = getArguments().getInt(PARAM_SHOW_ID);
            payMode = getArguments().getInt(PARAM_PAY_MODE);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recyclerview_episodes, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        labelNoData = (TextView) view.findViewById(R.id.labelNoData);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            int seasonid = getArguments().getInt(PARAM_SEASON_ID);
            int seasonNumber = getArguments().getInt(PARAM_SEASON_NUMBER);
            int position = getArguments().getInt(PARAM_POSITION);

            if (savedInstanceState != null)
                presenter.extractEpisodes(savedInstanceState);
            else presenter.loadEpisodes(showId, seasonid, seasonNumber, payMode);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Utilities.googleAnalytics(Constants.WEBVIEW_PLAYER_SCREEN);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.setActivityDestroyed(true);
        presenter.stopThreads();
    }

    @Override
    public void loadShow(Episode episode) {
    }

    @Override
    public void setFavoriteItem(boolean favoriteItem) {
    }

    @Override
    public void addFavorite() {
    }

    @Override
    public void onSeasonLoaded(int showId, SeasonsBean seasonsBean) {
        if (mEpisodes != null && seasonsBean != null) {
            mEpisodes.add(showId, seasonsBean);
            episodeAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onLoadedEpisodes(SortedListHashMap<Integer, SeasonsBean> episodes) {
        try {
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
            if (episodes.size() <= 0) {
                labelNoData.setVisibility(View.VISIBLE);
                labelNoData.setText("No episodes available for the current season");
            } else labelNoData.setVisibility(View.GONE);

            mEpisodes = episodes;
            episodeAdapter = new EpisodesAdapter(getActivity(), episodes,this);
            episodeAdapter.setFragmentManager(getChildFragmentManager());
            recyclerView.setAdapter(episodeAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onError() {
    }
}
