package com.selecttvapp.model;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ocs pl-79(17.2.2016) on 9/22/2016.
 */
public class Carousel implements Serializable {
    @SerializedName("items")
    private ArrayList<HorizontalListitemBean> items = new ArrayList<>();
    @SerializedName("title")
    private String title = "";
    @SerializedName("id")
    private Integer id = 0;
    @SerializedName("name")
    private String name = "";
    @SerializedName("slug")
    private String slug = "";
    @SerializedName("source")
    private String source = "";

    public Carousel(int id, String name, String source, ArrayList<HorizontalListitemBean> data_list) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.items = data_list;
    }

    public Carousel(JSONObject object) {
        try {
            if (object.has("id")) {
                id = object.getInt("id");
            }

            if (object.has("title")) {
                name = object.getString("title");
                Log.e("title title::", ":::" + name);
            }
            if (TextUtils.isEmpty(name)) {
                if (object.has("name")) {
                    name = object.getString("name");
                    Log.e("name ::", ":::" + name);
                }
            }

            if (object.has("slug")) {
                slug = object.getString("slug");
            }


            if (object.has("items")) {
                JSONArray itemsarray = object.getJSONArray("items");
                for (int j = 0; j < itemsarray.length(); j++) {
                    JSONObject itemsobject = itemsarray.getJSONObject(j);
                    HorizontalListitemBean horizontalListitemBean = new HorizontalListitemBean(itemsobject);
                    items.add(horizontalListitemBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        return items;
    }

    public void setData_list(ArrayList<HorizontalListitemBean> data_list) {
        this.items = data_list;
    }
}
