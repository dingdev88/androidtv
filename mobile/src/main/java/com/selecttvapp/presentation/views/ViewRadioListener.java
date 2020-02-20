package com.selecttvapp.presentation.views;

import com.selecttvapp.model.RadioDetailBean;
import com.selecttvapp.ui.bean.ListenGridBean;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 28-Nov-17.
 */

public interface ViewRadioListener {
    void loadGenreList(ArrayList<ListenGridBean> genreList);

    void loadRadioSubList(ArrayList<RadioDetailBean> listItems);

    void showRadioDetails(RadioDetailBean radioDetailBean);
}
