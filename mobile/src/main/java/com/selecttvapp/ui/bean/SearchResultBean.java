package com.selecttvapp.ui.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by babin on 7/28/2017.
 */

public class SearchResultBean implements Serializable {
    private String image, type, name, slug, categories;
    private int id;
    private boolean android_paid, android_free;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAndroid_paid() {
        return android_paid;
    }

    public void setAndroid_paid(boolean android_paid) {
        this.android_paid = android_paid;
    }

    public boolean isAndroid_free() {
        return android_free;
    }

    public void setAndroid_free(boolean android_free) {
        this.android_free = android_free;
    }

    public SearchResultBean(){
    }

    public SearchResultBean(JSONObject obj) {
        try {
            if (obj.has("image")) {
                setImage(obj.getString("image"));
            }
            if (obj.has("id")) {
                setId(obj.getInt("id"));
            }
            if (obj.has("name")) {
                setName(obj.getString("name"));
            }
            if (obj.has("type")) {
                setType(obj.getString("type"));
            }
            if (obj.has("android_paid")) {
                setAndroid_paid(obj.getBoolean("android_paid"));
            }
            if (obj.has("android_free")) {
                setAndroid_free(obj.getBoolean("android_free"));
            }
            if (obj.has("slug")) {
                setSlug(obj.getString("slug"));
            } else {
                setSlug("");
            }
            if (obj.has("categories")) {
                setCategories("");
                Object category_object = obj.get("categories");
                if (category_object instanceof JSONArray) {
                    JSONArray category_array = obj.getJSONArray("categories");
                    if (category_array.length() > 0) {
                        setCategories(category_array.get(0).toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
