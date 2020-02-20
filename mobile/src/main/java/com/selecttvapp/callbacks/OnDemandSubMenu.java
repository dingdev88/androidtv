package com.selecttvapp.callbacks;

import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.OnDemandList;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 26-Oct-17.
 */

public interface OnDemandSubMenu {
    void onMenuCategoriesCallback(ArrayList<OnDemandList> mOnDemandCategorylist);

    void setSubMenuList(int pagePosition, ArrayList<Carousel> carousels, CarouselsListener carouselsListener);

    void onClickViewMore(String itemId);
}
