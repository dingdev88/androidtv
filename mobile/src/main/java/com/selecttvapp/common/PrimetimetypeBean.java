package com.selecttvapp.common;

/**
 * Created by ${Madhan} on 6/3/2016.
 */
public class PrimetimetypeBean {
    private int id;
    private String description,title,image,name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PrimetimetypeBean(int id, String title, String name) {
        this.id = id;
        this.title = title;
        this.name = name;
    }
}
