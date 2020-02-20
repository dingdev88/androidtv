package com.selecttvapp.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Lenova on 5/6/2017.
 */

public class Movie implements Serializable {
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

    private String id = "";
    private String posterUrl = "";
    private ArrayList<String> trailers = new ArrayList<>();
    private ArrayList<String> relatedVideosId = new ArrayList<>();
    private String description = "";
    private String name = "";
    private String runtime = "";
    private String genre = "";
    private ArrayList<Actor> actors = new ArrayList<>();
    private String rating = "";
    private Sources sources;
    private String appsLinks = "";

    public Movie(JSONObject json) {
        try {
            if (json != null)
                appsLinks = json.toString();
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


            if (json.has(ACTORS)) {
                JSONArray array = json.getJSONArray(ACTORS);
                if (array.length() > 0)
                    for (int i = 0; i < array.length(); i++) {
                        Actor actor = new Actor(array.getJSONObject(i));
                        actors.add(actor);
                    }
            }

            if (json.has(SOURCES)) {
                sources = new Sources(json.getJSONObject(SOURCES));
            }

            if (json.has(TRAILERS)) {
                if (json.get(TRAILERS) instanceof JSONArray) {
                    JSONArray array = json.getJSONArray(TRAILERS);
                    if (array.length() > 0)
                        for (int i = 0; i < array.length(); i++) {
                            trailers.add(array.getString(i));
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

    public Sources getSources() {
        return sources;
    }

    public void setSources(Sources sources) {
        this.sources = sources;
    }


    public ArrayList<String> getRelatedVideosId() {
        return relatedVideosId;
    }

    public void setRelatedVideosId(ArrayList<String> relatedVideosId) {
        this.relatedVideosId = relatedVideosId;
    }

    public void setAppsLinks(String appsLinks) {
        this.appsLinks = appsLinks;
    }

    public String getAppsLinks() {
        return appsLinks;
    }
}
