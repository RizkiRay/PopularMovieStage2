package com.rizkiraynaldy.popularmoviestage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ray on 30/07/2017.
 */

public class ReviewsResponse {
    @SerializedName("id")
    int movieId;

    @SerializedName("page")
    int page;

    @SerializedName("results")
    List<Review> movieReviews;

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Review> getMovieReviews() {
        return movieReviews;
    }

    public void setMovieReviews(List<Review> movieReviews) {
        this.movieReviews = movieReviews;
    }
}
