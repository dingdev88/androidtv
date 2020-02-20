package com.selecttvapp.ui.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class SeasonsBean implements Serializable {
    private int id = 0,
            season_id = -1,
            season_number = -1;
    private String description = "",
            air_date = "",
            poster_url = "",
            name = "",
            linksCount = "",
            showLinks = "";
    private int payMode=0,showid=0;

    public int getShowid() {
        return showid;
    }

    public void setShowid(int showid) {
        this.showid = showid;
    }

    public int getPayMode() {
        return payMode;
    }

    public void setPayMode(int payMode) {
        this.payMode = payMode;
    }

    private boolean freeLinks = false,
            hasAuthenticatedItems = false;
    private ArrayList<String> subscriptions = new ArrayList<>();

    public SeasonsBean(int id, String description, String air_date, String poster_url, String name, int season_id, int season_number) {
        this.id = id;
        this.description = description;
        this.air_date = air_date;
        this.poster_url = poster_url;
        this.name = name;
        this.season_id = season_id;
        this.season_number = season_number;
    }

    public SeasonsBean(int id, String description, String air_date, String poster_url, String name, int season_id, int season_number, String linksCount, boolean freeLinks, ArrayList<String> subscriptions) {
        this.id = id;
        this.description = description;
        this.air_date = air_date;
        this.poster_url = poster_url;
        this.name = name;
        this.season_id = season_id;
        this.season_number = season_number;
        this.linksCount = linksCount;
        this.freeLinks = freeLinks;
        this.subscriptions = subscriptions;
    }

    public SeasonsBean(JSONObject jsonObject) {
        try {
            if (jsonObject.has("name")) {
                name = jsonObject.getString("name");
            }
            if (jsonObject.has("id")) {
                id = jsonObject.getInt("id");
            }
            if (jsonObject.has("description")) {
                description = jsonObject.getString("description");
            }
            if (jsonObject.has("image")) {
                poster_url = jsonObject.getString("image");
            }
            if (jsonObject.has("poster_url")) {
                poster_url = jsonObject.getString("poster_url");
            }
            if (jsonObject.has("air_date")) {
                air_date = jsonObject.getString("air_date");
            }
            if (jsonObject.has("season_id")) {
                season_id = jsonObject.getInt("season_id");
            }
            if (jsonObject.has("season_number")) {
                season_number = jsonObject.getInt("season_number");
            }
            if (jsonObject.has("free_links")) {
                freeLinks = jsonObject.getBoolean("free_links");
            }
            if (jsonObject.has("links_count")) {
                linksCount = jsonObject.getString("links_count");
            }

            ArrayList<String> subscriptions = new ArrayList<>();
            if (jsonObject.has("subscriptions")) {
                JSONArray jsonArray = jsonObject.getJSONArray("subscriptions");
                for (int j = 0; j < jsonArray.length(); j++) {
                    subscriptions.add(jsonArray.getString(j));
                }

            }
            this.subscriptions = subscriptions;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSeason_id() {
        return season_id;
    }

    public void setSeason_id(int season_id) {
        this.season_id = season_id;
    }

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinksCount() {
        return linksCount;
    }

    public void setLinksCount(String linksCount) {
        this.linksCount = linksCount;
    }

    public boolean getFreeLinks() {
        return freeLinks;
    }

    public void setFreeLinks(boolean freeLinks) {
        this.freeLinks = freeLinks;
    }


    public boolean hasAuthenticatedItems() {
        return hasAuthenticatedItems;
    }

    public void setHasAuthenticatedItems(boolean hasAuthenticatedItems) {
        this.hasAuthenticatedItems = hasAuthenticatedItems;
    }

    public ArrayList<String> getSubscriptionsList() {
        return subscriptions;
    }

    public void setSubscriptionsList(ArrayList<String> subscriptionsList) {
        this.subscriptions = subscriptionsList;
    }

    public String getShowLinks() {
        return showLinks;
    }

    public void setShowLinks(String showLinks) {
        this.showLinks = showLinks;
    }
}

