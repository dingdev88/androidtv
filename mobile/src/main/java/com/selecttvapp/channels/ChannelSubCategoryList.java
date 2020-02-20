package com.selecttvapp.channels;

import java.io.Serializable;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/12/2016.
 */
public class ChannelSubCategoryList implements Serializable {

    private String parent, name, slug;
    private boolean leaf;

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
