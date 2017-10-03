package com.rizkiraynaldy.popularmoviestage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ray on 30/07/2017.
 */

public class TrailerResponse {
    @SerializedName("id")
    int id;

    @SerializedName("results")
    List<Trailer> trailers;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }
}
