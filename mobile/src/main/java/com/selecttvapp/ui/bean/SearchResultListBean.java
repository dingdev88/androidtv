package com.selecttvapp.ui.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by babin on 7/28/2017.
 */

public class SearchResultListBean implements Serializable {
    private int id;
    private String name,source;
    private ArrayList<SearchResultBean> data_list;

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

    public ArrayList<SearchResultBean> getData_list() {
        return data_list;
    }

    public void setData_list(ArrayList<SearchResultBean> data_list) {
        this.data_list = data_list;
    }
    public SearchResultListBean(int id, String name, String source, ArrayList<SearchResultBean> data_list) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.data_list = data_list;
    }
}
