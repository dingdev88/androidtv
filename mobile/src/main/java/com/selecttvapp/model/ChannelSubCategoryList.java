package com.selecttvapp.model;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/12/2016.
 */
public class ChannelSubCategoryList {

    private int id;
    private String parent_id,image,name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChannelSubCategoryList(int id, String parent_id, String image, String name) {
        this.id = id;
        this.parent_id = parent_id;
        this.image = image;

        this.name = name;
    }
}
