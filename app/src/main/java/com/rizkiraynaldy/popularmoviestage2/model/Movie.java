package com.rizkiraynaldy.popularmoviestage2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ray on 14/06/2017.
 */

public class Movie implements Parcelable{
    @SerializedName("id")
    public int id;
    @SerializedName("original_title")
    public String originalTitle;
    @SerializedName("poster_path")
    public String moviePoster;
    @SerializedName("overview")
    public String plotSynopsis;
    @SerializedName("vote_average")
    public String userRating;
    @SerializedName("release_date")
    public String releaseDate;

    public static final String IMAGE_URL = "http://image.tmdb.org/t/p/w500/";

    protected Movie(Parcel in) {
        id = in.readInt();
        originalTitle = in.readString();
        moviePoster = in.readString();
        plotSynopsis = in.readString();
        userRating = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(moviePoster);
        dest.writeString(plotSynopsis);
        dest.writeString(userRating);
        dest.writeString(releaseDate);
    }
}
