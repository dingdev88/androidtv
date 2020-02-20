package com.selecttvapp.model;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by ${Madhan} on 5/18/2016.
 */
public class RadioDetailBean implements Serializable {

    String website, slogan, description, stream, twitter, frequency, wikipedia, phone, email, facebook, image, address, bitrate, slug, name;
    int id;

    public RadioDetailBean() {

    }

    public RadioDetailBean(String website, String slogan, String description, String stream, String twitter, String frequency, String wikipedia, String phone, String email, String facebook, String image, String address, String bitrate, String slug, String name, int id) {
        this.website = website;
        this.slogan = slogan;
        this.description = description;
        this.stream = stream;
        this.twitter = twitter;
        this.frequency = frequency;
        this.wikipedia = wikipedia;
        this.phone = phone;
        this.email = email;
        this.facebook = facebook;
        this.image = image;
        this.address = address;
        this.bitrate = bitrate;
        this.slug = slug;
        this.name = name;
        this.id = id;

    }

    public RadioDetailBean(JSONObject response) {
        try {
            if (response.has("website"))
                website = response.getString("website");
            if (response.has("slogan"))
                slogan = response.getString("slogan");
            if (response.has("description"))
                description = response.getString("description");
            if (response.has("stream"))
                stream = response.getString("stream");
            if (response.has("twitter"))
                twitter = response.getString("twitter");
            if (response.has("frequency"))
                frequency = response.getString("frequency");
            if (response.has("wikipedia"))
                wikipedia = response.getString("wikipedia");
            if (response.has("id"))
                id = response.getInt("id");
            if (response.has("email"))
                email = response.getString("email");
            if (response.has("phone"))
                phone = response.getString("phone");
            if (response.has("facebook"))
                facebook = response.getString("facebook");
            if (response.has("image"))
                image = response.getString("image");
            if (response.has("bitrate"))
                bitrate = response.getString("bitrate");
            if (response.has("address"))
                address = response.getString("address");
            if (response.has("slug"))
                slug = response.getString("slug");
            if (response.has("name"))
                name = response.getString("name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getWikipedia() {
        return wikipedia;
    }

    public void setWikipedia(String wikipedia) {
        this.wikipedia = wikipedia;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
