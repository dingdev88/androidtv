package com.selecttvapp.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ocs pl-79(17.2.2016) on 4/13/2016.
 */
public class HorizontalListitemBean implements Serializable {
    @SerializedName("carousel_image")
    private String image = "";
    @SerializedName("type")
    private String type = "";
    @SerializedName("id")
    private Integer id = 0;
    @SerializedName("name")
    private String name = "";
    @SerializedName("slug")
    private String slug = "";
    @SerializedName("url")
    private String url = "";

    public HorizontalListitemBean(int id, String image, String type, String name) {
        this.id = id;
        this.image = image;
        this.type = type;
        this.name = name;
    }

    public HorizontalListitemBean(JSONObject response) {
        try {
            if (response.has("id"))
                id = response.getInt("id");
            if (response.has("name"))
                name = response.getString("name");
            if (response.has("type"))
                type = response.getString("type");
            if (response.has("carousel_image"))
                image = response.getString("carousel_image");
            if (image.isEmpty())
                if (response.has("image"))
                    image = response.getString("image");
            if (response.has("slug"))
                slug = response.getString("slug");
            if (response.has("url"))
                url = response.getString("url");

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
