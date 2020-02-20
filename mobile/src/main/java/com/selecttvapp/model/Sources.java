package com.selecttvapp.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Lenova on 5/6/2017.
 */

public class Sources implements Serializable {
    private final String MOBILE = "mobile";
    private final String ANDROID = "android";
    private Android android;

    public Sources(JSONObject json) {
        try {
            if (json.has(MOBILE))
                if (json.getJSONObject(MOBILE).has(ANDROID))
                    android = new Android(json.getJSONObject(MOBILE).getJSONObject(ANDROID));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Android getAndroid() {
        return android;
    }

    public void setAndroid(Android android) {
        this.android = android;
    }


}
