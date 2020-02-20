package com.selecttvapp.channels;


import java.util.ArrayList;


/**
 * Created by babin on 7/4/2017.
 */

public interface ChannelApiListener {
    public void onChannelListLoaded(String categorySlug,ArrayList<ChannelScheduler> channelList, boolean isDialog);

    void onNetworkError();
}
