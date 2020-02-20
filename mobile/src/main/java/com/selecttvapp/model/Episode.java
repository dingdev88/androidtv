package com.selecttvapp.model;

import com.selecttvapp.ui.bean.SeasonsBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Lenova on 5/3/2017.
 */

public class Episode implements Serializable {
    private final String ID = "id";
    private final String POSTER_URL = "poster_url";
    private final String DESCRIPTION = "description";
    private final String NAME = "name";
    private final String RUNTIME = "runtime";
    private final String GENRE = "genre";
    private final String RATING = "rating";
    private final String TRAILERS = "trailers";
    private final String ACTORS = "actors";
    private final String SOURCES = "sources";
    private final String IMAGE = "image";
    private final String NETWORK = "network";

    private String id = "";
    private String posterUrl = "";
    private ArrayList<String> trailers = new ArrayList<>();
    private String description = "";
    private String name = "";
    private String runtime = "";
    private String genre = "";
    private ArrayList<Actor> actors = new ArrayList<>();
    private ArrayList<SeasonsBean> seasonsList = new ArrayList<>();
    private String rating = "";
    private Network network;

    public Episode(JSONObject json) {
        try {
            if (json.has(ID))
                id = json.getString(ID);
            if (json.has(POSTER_URL))
                posterUrl = json.getString(POSTER_URL);
            if (json.has(DESCRIPTION))
                description = json.getString(DESCRIPTION);
            if (json.has(NAME))
                name = json.getString(NAME);
            if (json.has(RUNTIME))
                runtime = json.getString(RUNTIME);
            if (json.has(RATING))
                rating = json.getString(RATING);
            if (json.has(GENRE))
                genre = json.getString(GENRE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (json.has(NETWORK)) {
                JSONObject object = json.getJSONObject(NETWORK);
                Network network = new Network(object);
                this.network = network;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (json.has(ACTORS)) {
                JSONArray array = json.getJSONArray(ACTORS);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    if (object != null) {
                        Actor actor = new Actor(object);
                        this.actors.add(actor);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public ArrayList<String> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<String> trailers) {
        this.trailers = trailers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public void setActors(ArrayList<Actor> actors) {
        this.actors = actors;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setSeasonsList(ArrayList<SeasonsBean> seasonsList) {
        this.seasonsList = seasonsList;
    }

    public ArrayList<SeasonsBean> getSeasonsList() {
        return seasonsList;
    }
}



