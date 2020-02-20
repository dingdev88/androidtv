package com.selecttvapp.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Lenova on 5/3/2017.
 */

public class Actor implements Serializable {
    private final String ID = "id";
    private final String IMAGE = "image";
    private final String NAME = "name";

    private String id;
    private String name;
    private String image = "";

    public Actor() {

    }

    public Actor(JSONObject json) {
        try {
            if (json.has(ID))
                setId(json.getString(ID));
            if (json.has(NAME))
                setName(json.getString(NAME));
            if (json.has(IMAGE))
                if (!json.getString(IMAGE).equalsIgnoreCase("") && !json.getString(IMAGE).equalsIgnoreCase("null"))
                    setImage(json.getString(IMAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
