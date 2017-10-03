package com.rizkiraynaldy.popularmoviestage2.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ray on 30/07/2017.
 */
public class Review {
    @SerializedName("id")
    String userId;
    @SerializedName("author")
    String authorName;
    @SerializedName("content")
    String content;
    @SerializedName("url")
    String url;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
