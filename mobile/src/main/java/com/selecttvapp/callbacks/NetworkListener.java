package com.selecttvapp.callbacks;

import com.selecttvapp.model.CauroselsItemBean;
import com.selecttvapp.model.Network;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 15-Nov-17.
 */

public interface NetworkListener {
    void onLoadNetworkList(ArrayList<CauroselsItemBean> listItems, Network network);

    void onClickItem(Network network);
}
