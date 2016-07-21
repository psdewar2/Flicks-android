package com.psd.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by PSD on 7/17/16.
 */
public class Movie {

    long movieId;
    String posterPath;
    String backdropPath;
    String originalTitle;
    String overview;
    double popularity;
    double rating;
    boolean isPopular;

    public long getMovieId() {
        return movieId;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w500/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w1280/%s", backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() { return popularity; }

    public double getRating() { return rating; }

    public boolean isPopular() { return isPopular; }

    public Movie(JSONObject jsonObject) throws JSONException {
        this.movieId = jsonObject.getLong("id");
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.popularity = jsonObject.getDouble("popularity");
        this.rating = jsonObject.getDouble("vote_average");
        this.isPopular = this.rating >= 5;
    }

    //iterates through each element and converts each into movie information
    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
