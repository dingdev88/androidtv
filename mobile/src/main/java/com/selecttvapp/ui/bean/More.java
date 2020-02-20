package com.selecttvapp.ui.bean;

import org.json.JSONObject;

/**
 * Created by ${Madhan} on 9/23/2016.
 */

public class More {

    String url, display_mode, name, weight, tags, device, image, description, id, slug;

    public More(String url, String display_mode, String name, String weight, String tags, String device, String image, String description, String id, String slug) {
        this.url = url;
        this.display_mode = display_mode;
        this.name = name;
        this.weight = weight;
        this.tags = tags;
        this.device = device;
        this.image = image;
        this.description = description;
        this.id = id;
        this.slug = slug;
    }

    public More(JSONObject response) {
        try {
            if (response.has("url"))
                url = response.getString("url");
            if (response.has("display_mode"))
                display_mode = response.getString("display_mode");
            if (response.has("name"))
                name = response.getString("name");
            if (response.has("weight"))
                weight = response.getString("weight");
            if (response.has("tags"))
                tags = response.getString("tags");
            if (response.has("device"))
                device = response.getString("device");
            if (response.has("image"))
                image = response.getString("image");
            if (response.has("description"))
                description = response.getString("description");
            if (response.has("id"))
                id = response.getString("id");
            if (response.has("slug"))
                slug = response.getString("slug");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplay_mode() {
        return display_mode;
    }

    public void setDisplay_mode(String display_mode) {
        this.display_mode = display_mode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
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
