package com.selecttvapp.model;

import java.util.ArrayList;

/**
 * Created by ocs on 6/7/2017.
 */

public class SideMenu {

    private String name = "";
    private String url;
    private String type = "";
    private String slug = "";
    private int menuPosition = -1;
    private ArrayList<SideMenuChild> sideMenuChild = new ArrayList<>();
    private boolean isClick;

    public SideMenu() {
    }

    public SideMenu(String name, String url, String type, String slug, ArrayList<SideMenuChild> sideMenuChild, boolean isClick) {
        this.name = name;
        this.url = url;
        this.type = type;
        this.slug = slug;
        this.sideMenuChild = sideMenuChild;
        this.isClick = isClick;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    public void setMenuPosition(int menuPosition) {
        this.menuPosition = menuPosition;
    }

    public int getMenuPosition() {
        return menuPosition;
    }
}
