package com.selecttvapp.channels;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by babin on 8/18/2017.
 */

public class ChannelListByCaegories  implements Serializable {
    private String categorySlug;
    private ArrayList<ChannelScheduler> channelsList;

    public String getCategorySlug() {
        return categorySlug;
    }

    public void setCategorySlug(String categorySlug) {
        this.categorySlug = categorySlug;
    }

    public ArrayList<ChannelScheduler> getChannelsList() {
        return channelsList;
    }

    public void setChannelsList(ArrayList<ChannelScheduler> channelsList) {
        this.channelsList = channelsList;
    }
}
