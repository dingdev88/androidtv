package com.selecttvapp.model;

import java.util.ArrayList;

public class SideMenuChild{
    private String id;
    String name;
    private String url;
    private String type="";
    private String slug="";
    private ArrayList<SideMenuChild> sideMenuChild=new ArrayList<>();
    private boolean isClick;

    public SideMenuChild() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SideMenuChild(String id, String name, String url, String type, String slug, ArrayList<SideMenuChild> sideMenuChild, boolean isClick) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.type = type;
        this.slug = slug;
        this.sideMenuChild = sideMenuChild;
        this.isClick = isClick;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<SideMenuChild> getSideMenuChild() {
        return sideMenuChild;
    }

    public void setSideMenuChild(ArrayList<SideMenuChild> sideMenuChild) {
        this.sideMenuChild = sideMenuChild;
    }
}