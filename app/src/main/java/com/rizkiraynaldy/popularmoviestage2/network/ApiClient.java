package com.rizkiraynaldy.popularmoviestage2.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ray on 14/06/2017.
 */

public class ApiClient {
    public static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String API_KEY = "0262e82dc579654e6dbff5d9e1e39b51";
    public static String getYoutubeImageLink(String youtubeKey){
        return "https://img.youtube.com/vi/" + youtubeKey + "/maxresdefault.jpg";
    }
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
