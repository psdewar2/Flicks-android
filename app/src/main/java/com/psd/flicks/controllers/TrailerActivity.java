package com.psd.flicks.controllers;

import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.psd.flicks.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TrailerActivity extends YouTubeBaseActivity {
    final String YT_API_KEY = "AIzaSyABSIKEyKnWllU_7rVHcNFhvSu2EdrifkE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        //get intent data
        long movieId = getIntent().getLongExtra("movieId", 0);
        String movieTrailersUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        final YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        //initialize http client
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(movieTrailersUrl, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieTrailerResults;
                try {
                    movieTrailerResults = response.getJSONArray("results");
                    //preventing any crashes by checks for null values
                    if (movieTrailerResults.getJSONObject(0) != null) {
                        final String youTubeVideoKey = movieTrailerResults.getJSONObject(0).getString("key");
                        youTubePlayerView.initialize(YT_API_KEY, new YouTubePlayer.OnInitializedListener() {

                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                Log.d("Playing ", b + youTubeVideoKey);
                                youTubePlayer.setFullscreen(true);
                                youTubePlayer.loadVideo(youTubeVideoKey);

                                //youTubePlayer.loadVideo(youTubeVideoKey);
                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                                Log.e("Provider ", provider.toString());
                                Log.e("Play error ", youTubeInitializationResult.toString());
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });



    }
}
