package com.selecttvapp.personalization;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Appsolute dev on 29-Jul-17.
 */

public class PersonalizationShows {

    private ArrayList<PersonalizationItem> items = new ArrayList<>();
    private ArrayList<String> mFavoriteItems = new ArrayList<>();
    private String id;
    private String name;
    private String slug;
    private String image;
    private String viewMoreLink;
    private String title;

    public PersonalizationShows(JSONObject json) {
        try {
            if (json.has("id"))
                id = json.getString("id");
            if (json.has("name"))
                name = json.getString("name");
            if (json.has("slug"))
                slug = json.getString("slug");
            if (json.has("title"))
                title = json.getString("title");
            if (json.has("image"))
                image = json.getString("image");
            if (json.has("view_more_link"))
                viewMoreLink = json.getString("view_more_link");
            if (json.has("items")) {
                JSONArray array = json.getJSONArray("items");
                for (int i = 0; i < array.length(); i++) {
                    PersonalizationItem item = new PersonalizationItem(array.getJSONObject(i));
                    items.add(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<PersonalizationItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<PersonalizationItem> items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getViewMoreLink() {
        return viewMoreLink;
    }

    public void setViewMoreLink(String viewMoreLink) {
        this.viewMoreLink = viewMoreLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getFavoriteItems() {
        return mFavoriteItems;
    }

    public void setFavoriteItems(ArrayList<String> mFavoriteItems) {
        this.mFavoriteItems = mFavoriteItems;
    }
}