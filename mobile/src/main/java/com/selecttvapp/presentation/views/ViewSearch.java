package com.selecttvapp.presentation.views;

import com.selecttvapp.ui.bean.SearchResultBean;
import com.selecttvapp.ui.bean.SearchResultListBean;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 29-Nov-17.
 */

public interface ViewSearch {
    void onSuccess(ArrayList<SearchResultListBean> searchList);

    void onError(Exception e);

    void loadItem(SearchResultBean item);
}
