package com.rizkiraynaldy.popularmoviestage2.network;

import com.rizkiraynaldy.popularmoviestage2.model.MovieResponse;
import com.rizkiraynaldy.popularmoviestage2.model.ReviewsResponse;
import com.rizkiraynaldy.popularmoviestage2.model.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ray on 14/06/2017.
 */

public interface ApiInterface {
    @GET("movie/popular")
    Call<MovieResponse> getMoviePopular(@Query("api_key") String apiKey, @Query("page") int page);
    @GET("movie/top_rated")
    Call<MovieResponse> getMovieTopRated(@Query("api_key") String apiKey, @Query("page") int page);
    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("movie_id")String movieId, @Query("api_key") String apiKey);
    @GET("movie/{movie_id}/reviews")
    Call<ReviewsResponse> getMovieReviews(@Path("movie_id")String movieId, @Query("api_key") String apiKey);
}
