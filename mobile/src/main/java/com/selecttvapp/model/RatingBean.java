package com.selecttvapp.model;

/**
 * Created by Ocs pl-79(17.2.2016) on 9/26/2016.
 */
public class RatingBean {
    private String slug,name,filter;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RatingBean(String slug, String name, String filter, int id) {
        this.slug = slug;
        this.name = name;
        this.filter = filter;
        this.id = id;
    }

    private int id;
}
