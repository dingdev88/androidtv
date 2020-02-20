package com.selecttvapp.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Lenova on 5/6/2017.
 */


public class Format  implements Serializable {

    private final String TYPE = "type";
    private final String PRE_ORDER = "pre_order";
    private final String FORMAT = "format";
    private final String PRICE = "price";

    private String type;
    private Boolean preOrder;
    private String format;
    private String price;

    public Format(JSONObject json) {
        try {
            if (json.has(TYPE))
                type = json.getString(TYPE);
            if (json.has(PRE_ORDER))
                preOrder = json.getBoolean(PRE_ORDER);
            if (json.has(FORMAT))
                format = json.getString(FORMAT);
            if (json.has(PRICE))
                price = json.getString(PRICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getPreOrder() {
        return preOrder;
    }

    public void setPreOrder(Boolean preOrder) {
        this.preOrder = preOrder;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
