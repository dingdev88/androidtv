package com.selecttvapp.common;

/**
 * Created by ${Madhan} on 6/3/2016.
 */
public class PrimeTimeData {


    private int id;
    private String image,type,name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public PrimeTimeData(int id, String image, String type, String name) {
        this.id = id;
        this.image = image;
        this.type = type;
        this.name = name;
    }
}
