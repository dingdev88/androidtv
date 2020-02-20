package com.selecttvapp.ui.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ${Madhan} on 9/22/2016.
 */

public class ListenGridBean implements Serializable {
    int id=0;
    String name="", imageUrl="";

    public ListenGridBean(int id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public ListenGridBean(JSONObject response) {
        try {
            if (response.has("id"))
                id = response.getInt("id");
            if (response.has("name"))
                name = response.getString("name");
            if (response.has("image"))
                imageUrl = response.getString("image");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
