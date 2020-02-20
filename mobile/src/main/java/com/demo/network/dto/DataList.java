package com.demo.network.dto;

import com.demo.network.model.Data;

import java.util.List;

public class DataList {
    public List<Data> getListData() {
        return listData;
    }

    public void setListData(List<Data> listData) {
        this.listData = listData;
    }

    private List<Data> listData;

}
