package com.selecttvapp.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenova on 5/6/2017.
 */


public class Android implements Serializable {
    private final String FREE = "free";
    private final String PAID = "paid";

    private List<Free> freeList = new ArrayList<>();
    private List<Paid> paidList = new ArrayList<>();

    public Android(JSONObject json) {
        try {
            if (json.has(FREE)) {
                JSONArray array = json.getJSONArray(FREE);
                if (array.length() > 0)
                    for (int i = 0; i < array.length(); i++) {
                        Free free = new Free(array.getJSONObject(i));
                        freeList.add(free);
                    }
            }

            if (json.has(PAID)) {
                JSONArray array = json.getJSONArray(PAID);
                if (array.length() > 0)
                    for (int i = 0; i < array.length(); i++) {
                        Paid paid = new Paid(array.getJSONObject(i));
                        paidList.add(paid);
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Free> getFree() {
        return freeList;
    }

    public void setFree(List<Free> free) {
        this.freeList = free;
    }

    public List<Paid> getPaid() {
        return paidList;
    }

    public void setPaid(List<Paid> paid) {
        this.paidList = paid;
    }

}