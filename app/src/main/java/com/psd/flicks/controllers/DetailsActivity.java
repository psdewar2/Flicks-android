package com.psd.flicks.controllers;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

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

public class DetailsActivity extends YouTubeBaseActivity {
    //ImageView backdropImageView;
    final String YT_API_KEY = "AIzaSyABSIKEyKnWllU_7rVHcNFhvSu2EdrifkE";
    TextView tvDetailsTitle, tvDetailsPopularity, tvDetailsOverview;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //get intent data
        long movieId = getIntent().getLongExtra("movieId", 0);
        String movieTitle = getIntent().getStringExtra("movieTitle");
        double movieRating = getIntent().getDoubleExtra("movieRating", 5.0);
        double moviePopularity = getIntent().getDoubleExtra("moviePopularity", 1.0);
        String movieOverview = getIntent().getStringExtra("movieOverview");
        String movieTrailersUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";


        final YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.detailsBackdropImageView);

        //widgets
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(movieTrailersUrl, new JsonHttpResponseHandler() {
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
                                youTubePlayer.cueVideo(youTubeVideoKey);
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
        });
        tvDetailsTitle = (TextView) findViewById(R.id.tvDetailsTitle);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        tvDetailsPopularity = (TextView) findViewById(R.id.tvDetailsPopularity);
        tvDetailsOverview = (TextView) findViewById(R.id.tvDetailsOverview);

        //Picasso.with(this).load(movieBackdrop).placeholder(R.mipmap.ic_launcher).into(backdropImageView);
        tvDetailsTitle.setText(movieTitle);
        ratingBar.setRating((float) (movieRating));
        tvDetailsPopularity.setText(String.valueOf("Popularity: " + moviePopularity));
        tvDetailsOverview.setText(movieOverview);

    }
}
