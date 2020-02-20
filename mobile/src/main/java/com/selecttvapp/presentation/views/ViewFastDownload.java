package com.selecttvapp.presentation.views;

import com.selecttvapp.model.CategoryBean;
import com.selecttvapp.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 20-Dec-17.
 */

public interface ViewFastDownload extends BaseView {
    void loadAppsList(ArrayList<CategoryBean> appsList);
}
