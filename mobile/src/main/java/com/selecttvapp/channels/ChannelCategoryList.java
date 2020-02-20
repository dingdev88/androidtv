package com.selecttvapp.channels;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by Ocs pl-79(17.2.2016) on 5/12/2016.
 */
public class ChannelCategoryList implements Serializable {

    private String parent,slug,name;

    private boolean leaf;

    private ArrayList<ChannelCategoryList> subCategories;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public ArrayList<ChannelCategoryList> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<ChannelCategoryList> subCategories) {
        this.subCategories = subCategories;
    }
}

