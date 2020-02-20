package com.selecttvapp.ui.bean;

import com.selecttvapp.model.HorizontalListitemBean;

import java.util.ArrayList;

/**
 * Created by ${Madhan} on 9/23/2016.
 */

public class HorizontalListBean {
    private int id;
    private String name,source;
    private ArrayList<HorizontalListitemBean> data_list;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public ArrayList<HorizontalListitemBean> getData_list() {
        return data_list;
    }

    public void setData_list(ArrayList<HorizontalListitemBean> data_list) {
        this.data_list = data_list;
    }

    public HorizontalListBean(int id, String name, String source, ArrayList<HorizontalListitemBean> data_list) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.data_list = data_list;
    }
}
