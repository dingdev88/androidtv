package com.selecttvapp.model;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ocs pl-79(17.2.2016) on 5/18/2016.
 */
public class CauroselsItemBean implements Serializable {
    private String carousel_image = "", type = "", name = "";
    private int id = 0;

    public String getCarousel_image() {
        return carousel_image;
    }

    public void setCarousel_image(String carousel_image) {
        this.carousel_image = carousel_image;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CauroselsItemBean(){
    }

    public CauroselsItemBean(String carousel_image, String type, int id, String name) {
        this.carousel_image = carousel_image;
        this.type = type;

        this.name = name;
        this.id = id;
    }

    public CauroselsItemBean(JSONObject object) {
        try {
            if (object.has("id")) {
                id = object.getInt("id");
            }

            if (object.has("title")) {
                name = object.getString("title");
                Log.d("title title::", ":::" + name);
            }
            if (TextUtils.isEmpty(name)) {
                if (object.has("name")) {
                    name = object.getString("name");
                    Log.d("name ::", ":::" + name);
                }
            }
            if (object.has("type")) {
                type = object.getString("type");
            }
            if (object.has("poster_url")) {
                carousel_image = object.getString("poster_url");
            } else if (object.has("carousel_image")) {
                carousel_image = object.getString("carousel_image");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
