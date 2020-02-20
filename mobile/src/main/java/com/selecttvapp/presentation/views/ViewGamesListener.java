package com.selecttvapp.presentation.views;

import com.selecttvapp.model.Carousel;
import com.selecttvapp.ui.bean.SideMenu;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 29-Nov-17.
 */

public interface ViewGamesListener {
    void loadGames(ArrayList<Carousel> gamesList);

    void loadCategories(ArrayList<SideMenu> categories);
}
