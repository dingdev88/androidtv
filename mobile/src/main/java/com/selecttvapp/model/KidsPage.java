package com.selecttvapp.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Appsolute dev on 08-Sep-17.
 */

public class KidsPage implements Serializable {

    private ArrayList<Carousel> carousels = new ArrayList<>();
    private String name;
    private String url;
    private ArrayList<Slider> sliders = new ArrayList<>();
    private ArrayList<Object> linksLists = new ArrayList<>();
    private String slug;

    public KidsPage(JSONObject json) {
        try {
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("url"))
                url = json.getString("url");
            if (json.has("slug"))
                slug = json.getString("slug");
            if (json.has("sliders")) {
                JSONArray slidersJsonArray = json.getJSONArray("sliders");
                for (int i = 0; i < slidersJsonArray.length(); i++) {
                    JSONObject jsonObject = slidersJsonArray.getJSONObject(i);
                    if (jsonObject.has("items")) {
                        JSONArray sliderItemsJsonArray = jsonObject.getJSONArray("items");
                        for (int j = 0; j < sliderItemsJsonArray.length(); j++) {
                            JSONObject jsonObject1 = sliderItemsJsonArray.getJSONObject(j);
                            sliders.add(new Slider(jsonObject1));
                        }
                    }
                }
            }
            if (json.has("carousels")) {
                JSONArray carouselsJsonArray = json.getJSONArray("carousels");
                for (int i = 0; i < carouselsJsonArray.length(); i++) {
                    JSONObject jsonObject = carouselsJsonArray.getJSONObject(i);
                    carousels.add(new Carousel(jsonObject));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Carousel> getCarousels() {
        return carousels;
    }

    public void setCarousels(ArrayList<Carousel> carousels) {
        this.carousels = carousels;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Slider> getSliders() {
        return sliders;
    }

    public void setSliders(ArrayList<Slider> sliders) {
        this.sliders = sliders;
    }

    public ArrayList<Object> getLinksLists() {
        return linksLists;
    }

    public void setLinksLists(ArrayList<Object> linksLists) {
        this.linksLists = linksLists;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }


}
