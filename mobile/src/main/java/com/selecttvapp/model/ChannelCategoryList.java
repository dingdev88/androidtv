package com.selecttvapp.model;

import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/12/2016.
 */
public class ChannelCategoryList {

    private int id;
    private String parent_id,image,name;

    private ArrayList<ChannelSubCategoryList> subCategories;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ChannelSubCategoryList> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<ChannelSubCategoryList> subCategories) {
        this.subCategories = subCategories;
    }

    public ChannelCategoryList(String parent_id, int id, String image, String name, ArrayList<ChannelSubCategoryList> subCategories) {
        this.parent_id = parent_id;
        this.id = id;
        this.image = image;
        this.name = name;
        this.subCategories = subCategories;
    }
}
