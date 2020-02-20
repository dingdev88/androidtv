package com.selecttvapp.channels;


import java.io.Serializable;

/**
 * Created by babin on 7/4/2017.
 */

public class Streams implements Serializable {
    private String type,name,data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isFlash() {
        return flash;
    }

    public void setFlash(boolean flash) {
        this.flash = flash;
    }

    private boolean flash;
}
