package com.selecttvapp.ui.bean;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ${Madhan} on 9/23/2016.
 */

public class SideMenu implements Serializable {

    private String id = "", name = "", type = "", order = "";

    public SideMenu(String id, String name, String type, String order) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.order = order;
    }

    public SideMenu(JSONObject response) {
        try {
            if (response.has("id"))
                id = response.getString("id");
            if (response.has("title"))
                name = response.getString("title");
            if (TextUtils.isEmpty(name))
                if (response.has("name"))
                    name = response.getString("name");
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
