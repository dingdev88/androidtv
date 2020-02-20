package com.selecttvapp.callbacks;

import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.CategoryBean;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.Network;
import com.selecttvapp.model.RatingBean;
import com.selecttvapp.model.Slider;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 29-Aug-17.
 */

public interface OnDemandListener {
    void onLoadedSlidersAndCarousels(final ArrayList<Slider> sliders, final ArrayList<Carousel> carousels);

    void onRatingListCallback(ArrayList<RatingBean> ratingList);

    void onGridAdapterListCallback(ArrayList<CauroselsItemBean> listItems, String type);

    void onNetworkListCallback(ArrayList<CauroselsItemBean> listItems, String type);

    void onViewAllCallback(ArrayList<CauroselsItemBean> listItems, String viewAllId);

    void onNetworkDataLoadedCallback(ArrayList<CauroselsItemBean> listItems, Network network);

    void onSecondSpinnerListCallback(ArrayList<CategoryBean> secondSpinnerListItems);

    void onLoadMoreItemsCallback(ArrayList<CauroselsItemBean> listItems);

}

