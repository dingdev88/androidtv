package com.selecttvapp.model;

import org.json.JSONObject;

/**
 * Created by babin on 1/4/2017.
 */

public class UserSubscriptionBean {
    private String code = "";
    private String image_url = "";
    private String name = "";
    private String slug = "";
    private String gray_image_url = "";
    private int id=0;
    private boolean isSelected=false;

    public String getGray_image_url() {
        return gray_image_url;
    }

    public void setGray_image_url(String gray_image_url) {
        this.gray_image_url = gray_image_url;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
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

    public UserSubscriptionBean(String code, String image_url, String name, String slug, String gray_image_url, boolean isSelected) {
        this.code = code;
        this.image_url = image_url;
        this.name = name;
        this.slug = slug;
        this.isSelected = isSelected;
        this.gray_image_url = gray_image_url;
    }

    public UserSubscriptionBean(JSONObject json) {
        try {
            if (json.has("id"))
                code = json.getString("id");
            if (json.has("code"))
                code = json.getString("code");
            if (json.has("image_url"))
                image_url = json.getString("image_url");
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("slug"))
                slug = json.getString("slug");
            if (json.has("gray_image_url"))
                gray_image_url = json.getString("gray_image_url");
            if (json.has("subscribed"))
                isSelected = json.getBoolean("subscribed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
