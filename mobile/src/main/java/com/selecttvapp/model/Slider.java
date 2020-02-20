package com.selecttvapp.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ocs pl-79(17.2.2016) on 9/22/2016.
 */
public class Slider implements Serializable {
    private int id = 0;
    private String description = "", title = "", image = "", name = "", type = "",url="";

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Slider() {

    }

    public Slider(int id, String description, String title, String image, String name, String type) {
        this.id = id;
        this.description = description;
        this.title = title;
        this.image = image;
        this.name = name;
        this.type = type;

    }

    public Slider(JSONObject slider_object) {
        try {
            if (slider_object.has("id"))
                id = slider_object.getInt("id");

            if (slider_object.has("description"))
                description = slider_object.getString("description");

            if (slider_object.has("title"))
                title = slider_object.getString("title");

            if (slider_object.has("image"))
                image = slider_object.getString("image");

            if (slider_object.has("name"))
                name = slider_object.getString("name");

            if (slider_object.has("type"))
                type = slider_object.getString("type");
            if(slider_object.has("url"))
                url=slider_object.getString("url");
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
