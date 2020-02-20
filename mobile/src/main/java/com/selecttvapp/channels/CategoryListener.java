package com.selecttvapp.channels;


import java.util.ArrayList;


/**
 * Created by babin on 7/3/2017.
 */

public interface CategoryListener {
    public void onCategoriesLoaded(ArrayList<ChannelCategoryList> categorylist);
    public void onLoadingFailed();
}
