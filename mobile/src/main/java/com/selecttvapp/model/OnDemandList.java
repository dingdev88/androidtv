package com.selecttvapp.model;

import org.json.JSONObject;

/**
 * Created by Ocs pl-79(17.2.2016) on 4/12/2016.
 */
public class OnDemandList {

    private String id = "", name = "", type = "", order = "";

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

    public OnDemandList(String id, String name, String type, String order) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.order = order;
    }

    public OnDemandList(JSONObject json) {
        try {
            if (json.has("name")) {
                name = json.getString("name");
                if (name.equalsIgnoreCase("Primetime Anytime")) {
                    name = "Primetime";
                }
            }

            if (json.has("order"))
                order = json.getString("order");
            if (json.has("id"))
                id = json.getString("id");
            if (json.has("page"))
                type = json.getString("page");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
