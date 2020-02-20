package com.selecttvapp.model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ocs pl-79(17.2.2016) on 7/27/2016.
 */
public class GridBean implements Serializable {

    private String id="";
    private String name="";
    private String image="";
    private String description="";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public GridBean(String id, String name, String image, String description, String type) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.type = type;
    }

    public GridBean(JSONObject response) {
        try {
            if (response.has("id")) {
                id = response.getString("id");
            }
            if (response.has("name")) {
                name = response.getString("name");
            }

            if (response.has("carousel_image")) {
                image = response.getString("carousel_image");
            } else {
                if (response.has("image")) {
                    image = response.getString("image");
                }
            }


            if (response.has("type")) {
                type = response.getString("type");

                Log.d("type", "grid" + type);

            } else {
                type = "s";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}