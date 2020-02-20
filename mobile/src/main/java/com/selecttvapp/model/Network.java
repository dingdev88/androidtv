package com.selecttvapp.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Lenova on 5/3/2017.
 */

public class Network implements Serializable {

    @SerializedName("id")
    private String id = "";
    @SerializedName("name")
    private String name = "";
    @SerializedName("slug")
    private String slug = "";
    @SerializedName("slogan")
    private String slogan = "";
    @SerializedName("app_image")
    private String appImage = "";
    @SerializedName("start_time")
    private String startTime = "";
    @SerializedName("image")
    private String image = "";
    @SerializedName("description")
    private String description = "";
    @SerializedName("headquarters")
    private String headquarters = "";

    public Network(JSONObject json) {
        try {
            if (json.has("id"))
                id = json.getString("id");
            if (json.has("slug"))
                slug = json.getString("slug");
            if (json.has("slogan"))
                slogan = json.getString("slogan");
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("start_time"))
                startTime = json.getString("start_time");
            if (json.has("headquarters"))
                headquarters = json.getString("headquarters");
            if (json.has("app_image"))
                appImage = json.getString("app_image");
            if (json.has("image"))
                image = json.getString("image");
            if (json.has("description"))
                description = json.getString("description");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAppImage() {
        return appImage;
    }

    public void setAppImage(String appImage) {
        this.appImage = appImage;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

}