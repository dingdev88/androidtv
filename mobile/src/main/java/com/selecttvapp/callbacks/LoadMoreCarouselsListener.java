package com.selecttvapp.callbacks;

import com.selecttvapp.model.CauroselsItemBean;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 07-Sep-17.
 */

public interface LoadMoreCarouselsListener {
    void onLoadCarousels(ArrayList<CauroselsItemBean> listItems, String viewAllId);
}
