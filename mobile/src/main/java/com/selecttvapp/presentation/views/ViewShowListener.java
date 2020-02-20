package com.selecttvapp.presentation.views;

import com.selecttvapp.SortedListHashMap.SortedListHashMap;
import com.selecttvapp.model.Episode;
import com.selecttvapp.ui.bean.SeasonsBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Appsolute dev on 28-Nov-17.
 */

public interface ViewShowListener {
    void loadShow(Episode episode);

    void setFavoriteItem(boolean favoriteItem);

    void addFavorite();

    void onLoadedEpisodes(SortedListHashMap<Integer, SeasonsBean> episodes);

    void onSeasonLoaded(int showId, SeasonsBean seasonsBean);

    void onError();
}
