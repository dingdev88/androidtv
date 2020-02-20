package com.selecttvapp.presentation.views;

import com.selecttvapp.model.Carousel;
import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.OnDemandList;
import com.selecttvapp.model.Slider;
import com.selecttvapp.ui.base.BaseView;

import java.util.ArrayList;

/**
 * Created by ocspl-72 on 29/12/17.
 */

public interface ViewODSubscriptions extends BaseView {
    void loadCategories(ArrayList<OnDemandList> categories);

    void onPageChangeListener(int position);

    void loadSlidersAndCarousels(ArrayList<Slider> sliders, ArrayList<Carousel> carousels);

    void loadNetworkDetails(String networkId);

    void loadViewMoreList(String viewAllId, ArrayList<CauroselsItemBean> listItems);

    void onLoadMoreCarousels(ArrayList<CauroselsItemBean> listItems, String viewAllId);

    void onClickViewMore(String itemId);

    void onBackPressed();
}
