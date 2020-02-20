package com.selecttvapp.channels;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Created by babin on 7/4/2017.
 */

public class ProgramList implements Serializable {
    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    private String uuid,name,duration;
    private String start_at,end_at;

    public int getScroll_dealay() {
        return scroll_dealay;
    }

    public void setScroll_dealay(int scroll_dealay) {
        this.scroll_dealay = scroll_dealay;
    }

    private int scroll_dealay;

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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ArrayList<PlayList> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(ArrayList<PlayList> playlist) {
        this.playlist = playlist;
    }

    private ArrayList<PlayList> playlist;

}
