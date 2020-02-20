package com.selecttvapp.model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 7/13/2016.
 */

public class CategoryBean {

    @SerializedName("link")
    private String link = "";
    @SerializedName("name")
    private String name = "";
    @SerializedName("weight")
    private Integer weight = -1;
    @SerializedName("package")
    private String packageName = "";
    @SerializedName("image")
    private String image = "";
    @SerializedName("devices")
    private ArrayList<Integer> devices = new ArrayList<>();
    @SerializedName("slug")
    private String slug = "";
    @SerializedName("categories")
    private ArrayList<Integer> categories = new ArrayList<>();
    @SerializedName("size")
    private String size;
    @SerializedName("id")
    private int id = 0;

    public CategoryBean(String slug, String name, int id) {
        this.slug = slug;
        this.name = name;
        this.id = id;
    }

    public CategoryBean(String link, String name, int id, String packageName) {
        this.id = id;
        this.link = link;
        this.name = name;
        this.packageName = packageName;
    }


    public CategoryBean(JSONObject json) {
        try {
            if (json.has("id"))
                id = json.getInt("id");
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("slug"))
                slug = json.getString("slug");
            if (json.has("label")) {
                name = json.getString("label");
            }
            if (json.has("package_name")) {
                packageName = json.getString("package_name");
            }
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

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<Integer> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Integer> devices) {
        this.devices = devices;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ArrayList<Integer> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Integer> categories) {
        this.categories = categories;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
