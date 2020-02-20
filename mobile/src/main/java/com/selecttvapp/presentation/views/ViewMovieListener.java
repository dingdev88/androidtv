package com.selecttvapp.presentation.views;

import com.selecttvapp.model.Movie;

import org.json.JSONObject;

/**
 * Created by Appsolute dev on 23-Nov-17.
 */

public interface ViewMovieListener {
    void onLoadedMovieDetails(Movie movie);

    void makeAppsLists(JSONObject response);

    void setFavoriteItem(boolean favoriteItem);

    void addFavorite();
}
