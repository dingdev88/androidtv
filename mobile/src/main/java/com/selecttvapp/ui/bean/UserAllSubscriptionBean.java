package com.selecttvapp.ui.bean;

import com.selecttvapp.model.HorizontalListitemBean;
import com.selecttvapp.model.UserSubscriptionBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by babin on 1/9/2017.
 */

public class UserAllSubscriptionBean implements Serializable {

    private ArrayList<HorizontalListitemBean> data_list = new ArrayList<>();
    private String code = "";
    private String image_url = "";
    private String name = "";
    private String slug = "";
    private String gray_image_url = "";
    private boolean isSelected = false;
    private int id = 0;

    public String getGray_image_url() {
        return gray_image_url;
    }

    public void setGray_image_url(String gray_image_url) {
        this.gray_image_url = gray_image_url;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ArrayList<HorizontalListitemBean> getData_list() {
        return data_list;
    }

    public void setData_list(ArrayList<HorizontalListitemBean> data_list) {
        this.data_list = data_list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserAllSubscriptionBean(int id, String code, String image_url, String name, String slug, String gray_image_url, boolean isSelected, ArrayList<HorizontalListitemBean> data_list) {
        this.id = id;
        this.code = code;
        this.image_url = image_url;
        this.name = name;
        this.slug = slug;
        this.isSelected = isSelected;
        this.gray_image_url = gray_image_url;
        this.data_list = data_list;
    }

    public UserAllSubscriptionBean(JSONObject json, UserSubscriptionBean item) {
        try {
            if (json.has("id"))
                id = json.getInt("id");
            if (json.has("code"))
                code = json.getString("code");
            if (json.has("image_url"))
                image_url = json.getString("image_url");
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("slug"))
                slug = json.getString("slug");
            if (json.has("gray_image_url"))
                gray_image_url = json.getString("gray_image_url");
            if (json.has("subscribed"))
                isSelected = json.getBoolean("subscribed");

            if (id == 0) {
                if (code.equalsIgnoreCase("HULU")) {
                    id = 75;
                } else if (code.equalsIgnoreCase("NETFLIX")) {
                    id = 303;
                } else if (code.equalsIgnoreCase("AMZN")) {
                    id = 537;
                } else if (code.equalsIgnoreCase("HBO_NOW")) {
                    id = 54;
                } else if (code.equalsIgnoreCase("SHOWTIME")) {
                    id = 120;
                } else if (code.equalsIgnoreCase("CBS")) {
                    id = 17;
                }
            }

            if (json.has("shows")) {
                JSONArray show_array = json.getJSONArray("shows");
                if (show_array != null && show_array.length() > 0) {
                    for (int j = 0; j < show_array.length(); j++) {
                        JSONObject show_object = show_array.getJSONObject(j);
                        HorizontalListitemBean horizontalListitemBean = new HorizontalListitemBean(show_object);
                        horizontalListitemBean.setType("S");
                        data_list.add(horizontalListitemBean);
                    }
                }
            }

            if (item != null) {
                if (image_url.isEmpty())
                    image_url = item.getImage_url();
                if (gray_image_url.isEmpty())
                    gray_image_url = item.getGray_image_url();
                if (!json.has("subscribed"))
                    isSelected = item.isSelected();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
