package com.selecttvapp.personalization;

import org.json.JSONObject;

/**
 * Created by Appsolute dev on 29-Jul-17.
 */

public class PersonalizationItem {

    private String url;
    private String image;
    private String id;
    private String name;

    public PersonalizationItem(JSONObject json) {
        try {
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("id"))
                id = json.getString("id");
            if (json.has("image"))
                image = json.getString("image");
            if (json.has("url"))
                url = json.getString("url");
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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

}
