package com.selecttvapp.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 6/13/2016.
 */
public class FavoriteBean implements Serializable {
    private int id;
    private String slug;
    private String image;
    private String logo;
    private String name;
    private String rating;
    private String runtime;
    private String description;
    private ArrayList<String> channelCategories = new ArrayList<>();
    private String url;

    public FavoriteBean(JSONObject json) {
        try {
            if (json.has("slug"))
                slug = json.getString("slug");
            if (json.has("id"))
                id = json.getInt("id");
            if (json.has("image"))
                image = json.getString("image");
            if (json.has("logo"))
                logo = json.getString("logo");
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("rating"))
                rating = json.getString("rating");
            if (json.has("url"))
                url = json.getString("url");
            if (json.has("runtime"))
                runtime = json.getString("runtime");
            if (json.has("description"))
                description = json.getString("description");
            if (json.has("categories")) {
                JSONArray jsonArray = json.getJSONArray("categories");
                for (int i = 0; i < jsonArray.length(); i++)
                    channelCategories.add(jsonArray.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FavoriteBean(String slug, String image, String name, int id, String rating, String runtime, String description, String url) {
        this.slug = slug;
        this.image = image;
        this.name = name;
        this.id = id;
        this.rating = rating;
        this.runtime = runtime;
        this.description = description;
        this.url = url;

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<String> getChannelCategories() {
        return channelCategories;
    }

    public void setChannelCategories(ArrayList<String> channelCategories) {
        this.channelCategories = channelCategories;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }
}
