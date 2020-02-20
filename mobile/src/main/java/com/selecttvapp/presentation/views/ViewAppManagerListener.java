package com.selecttvapp.presentation.views;

import com.selecttvapp.model.CategoryBean;
import com.selecttvapp.model.HorizontalListAppManager;
import com.selecttvapp.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 08-Dec-17.
 */

public interface ViewAppManagerListener extends BaseView {
    void loadAppCategories(ArrayList<CategoryBean> appCategoriesList);

    void loadCategoryItems(ArrayList<HorizontalListAppManager> categoryItems);
}
