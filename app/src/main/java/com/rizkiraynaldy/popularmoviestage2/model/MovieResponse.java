package com.rizkiraynaldy.popularmoviestage2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ray on 14/06/2017.
 */

public class MovieResponse {
    @SerializedName("page")
    private int page;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<Movie> moviePopulars;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Movie> getMoviePopulars() {
        return moviePopulars;
    }

    public void setMoviePopulars(List<Movie> moviePopulars) {
        this.moviePopulars = moviePopulars;
    }
}
