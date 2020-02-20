package com.selecttvapp.presentation.views;

import com.selecttvapp.model.FavoriteBean;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Appsolute dev on 29-Nov-17.
 */

public interface ViewMyInterestListener {
    void loadFavoriteList(HashMap<String, ArrayList<FavoriteBean>> favoriteList);
    void showSessionExpiredDialog();
}
