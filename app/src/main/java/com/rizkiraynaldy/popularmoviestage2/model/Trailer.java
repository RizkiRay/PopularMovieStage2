package com.rizkiraynaldy.popularmoviestage2.model;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ray on 30/07/2017.
 */

public class Trailer {
    @SerializedName("id")
    String videoId;

    @SerializedName("key")
    String youtubeKey;

    @SerializedName("name")
    String name;

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public void setYoutubeKey(String youtubeKey) {
        this.youtubeKey = youtubeKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
