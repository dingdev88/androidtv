package com.selecttvapp.callbacks;

import com.selecttvapp.model.OnDemandList;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 31-Oct-17.
 */

public interface OnDemandsListener {
    void onLoadMenuCategories(ArrayList<OnDemandList> categoryList);
}
