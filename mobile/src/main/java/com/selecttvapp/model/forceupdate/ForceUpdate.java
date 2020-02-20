package com.selecttvapp.model.forceupdate;

/*
 * Created by Pradeep-OCS on 4/12/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForceUpdate {
    @Expose
    @SerializedName("id")
    public int id;
    @Expose
    @SerializedName("force_update")
    public boolean forceUpdate;
    @Expose
    @SerializedName("app")
    public String app;
    @Expose
    @SerializedName("link")
    public String link;
    @Expose
    @SerializedName("download_path")
    public String downloadPath;
    @Expose
    @SerializedName("platform")
    public String platform;
    @Expose
    @SerializedName("release_text")
    public String releaseText;
    @Expose
    @SerializedName("version")
    public String version;
    @Expose
    @SerializedName("release_date")
    public String releaseDate;
}
