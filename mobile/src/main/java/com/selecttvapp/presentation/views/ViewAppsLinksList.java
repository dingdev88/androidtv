package com.selecttvapp.presentation.views;

import com.selecttvapp.ui.bean.AppFormatBean;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Appsolute dev on 23-Nov-17.
 */

public interface ViewAppsLinksList {
    void showAppStoreDialog(AppFormatBean app);

    void setAppsList(Map<String, ArrayList<AppFormatBean>> appsLists);
}
