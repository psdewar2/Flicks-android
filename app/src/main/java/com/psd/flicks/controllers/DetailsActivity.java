package com.psd.flicks.controllers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.psd.flicks.R;
import com.squareup.picasso.Picasso;

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
        final String movieBackdrop = getIntent().getStringExtra("movieBackdrop");
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
                    ImageView backdropImageView = new ImageView(getApplicationContext());
                    ViewGroupUtils vgu = new ViewGroupUtils();
                    vgu.replaceView(youTubePlayerView, backdropImageView);
                    Picasso.with(getApplicationContext()).load(movieBackdrop).into(backdropImageView);
                }
            }
        });
        tvDetailsTitle = (TextView) findViewById(R.id.tvDetailsTitle);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        tvDetailsPopularity = (TextView) findViewById(R.id.tvDetailsPopularity);
        tvDetailsOverview = (TextView) findViewById(R.id.tvDetailsOverview);


        tvDetailsTitle.setText(movieTitle);
        ratingBar.setRating((float) (movieRating));
        tvDetailsPopularity.setText(String.valueOf("Popularity: " + moviePopularity));
        tvDetailsOverview.setText(movieOverview);

    }

    //inner class that enables replacement if trailer doesn't exist for movie
    class ViewGroupUtils {

        public ViewGroup getParent(View view) {
            return (ViewGroup)view.getParent();
        }

        public void removeView(View view) {
            ViewGroup parent = getParent(view);
            if(parent != null) {
                parent.removeView(view);
            }
        }

        public void replaceView(View currentView, View newView) {
            ViewGroup parent = getParent(currentView);
            if(parent == null) {
                return;
            }
            final int index = parent.indexOfChild(currentView);
            removeView(currentView);
            removeView(newView);
            parent.addView(newView, index);
        }
    }
}
