package com.selecttvapp.callbacks;

import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.HorizontalListitemBean;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 11-Sep-17.
 */

public interface CarouselsListener {
    void onLoadCarousels(ArrayList<CauroselsItemBean> listItems, final String type);

    void onClickItem(HorizontalListitemBean item);

    void viewMore(String value);
}
