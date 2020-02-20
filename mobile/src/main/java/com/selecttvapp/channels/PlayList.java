package com.selecttvapp.channels;


import java.io.Serializable;

/**
 * Created by babin on 7/4/2017.
 */

public class PlayList implements Serializable {
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPlay_from() {
        return play_from;
    }

    public void setPlay_from(String play_from) {
        this.play_from = play_from;
    }

    public String getPlay_till() {
        return play_till;
    }

    public void setPlay_till(String play_till) {
        this.play_till = play_till;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    private String uuid,name,type,source,data,play_from,play_till,thumbnail;
}
