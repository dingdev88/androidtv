package com.selecttvapp.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenova on 5/11/2017.
 */

public class Free implements Serializable {
    private final String APP_DOWNLOAD_LINK = "app_download_link";
    private final String DISPLAY_NAME = "display_name";
    private final String SOURCE = "source";
    private final String APP_LINK = "app_link";
    private final String LINK = "link";
    private final String IMAGE = "image";
    private final String APP_REQUIRED = "app_required";
    private final String APP_NAME = "app_name";
    private final String FORMATS = "formats";

    private String source;
    private String link;
    private String appName;
    private String appDownloadLink;
    private List<Format> formats = new ArrayList<>();
    private String displayName;
    private String image;
    private Boolean appRequired;
    private Boolean appLink;

    public Free(JSONObject json) {
        try {
            if (json.has(APP_DOWNLOAD_LINK))
                setAppDownloadLink(json.getString(APP_DOWNLOAD_LINK));
            if (json.has(DISPLAY_NAME))
                setDisplayName(json.getString(DISPLAY_NAME));
            if (json.has(SOURCE))
                setSource(json.getString(SOURCE));
            if (json.has(APP_LINK))
                setAppLink(json.getBoolean(APP_LINK));
            if (json.has(LINK))
                setLink(json.getString(LINK));
            if (json.has(IMAGE))
                setImage(json.getString(IMAGE));
            if (json.has(APP_REQUIRED))
                setAppRequired(json.getBoolean(APP_REQUIRED));
            if (json.has(APP_NAME))
                setAppName(json.getString(APP_NAME));
            if (json.has(FORMATS)) {
                if (!json.isNull(FORMATS)) {
                    JSONArray arrayFormats = json.getJSONArray(FORMATS);
                    if (arrayFormats.length() > 0)
                        for (int j = 0; j < arrayFormats.length(); j++) {
                            Format format = new Format(arrayFormats.getJSONObject(j));
                            formats.add(format);
                        }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppDownloadLink() {
        return appDownloadLink;
    }

    public void setAppDownloadLink(String appDownloadLink) {
        this.appDownloadLink = appDownloadLink;
    }

    public List<Format> getFormats() {
        return formats;
    }

    public void setFormats(List<Format> formats) {
        this.formats = formats;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getAppRequired() {
        return appRequired;
    }

    public void setAppRequired(Boolean appRequired) {
        this.appRequired = appRequired;
    }

    public Boolean getAppLink() {
        return appLink;
    }

    public void setAppLink(Boolean appLink) {
        this.appLink = appLink;
    }
}
