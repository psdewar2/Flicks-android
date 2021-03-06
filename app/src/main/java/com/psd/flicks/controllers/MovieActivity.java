package com.psd.flicks.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.psd.flicks.R;
import com.psd.flicks.adapters.MovieAdapter;
import com.psd.flicks.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {
    String nowPlayingUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    ArrayList<Movie> movies;
    MovieAdapter movieAdapter;

    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvMovies) RecyclerView rvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this); //this eliminates usage of finding view by id

        //setup refresh listener which triggers new data loading
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //call swipeContainer.setRefreshing(false) once the network request has completed successfully.
                fetchMoviesAsync();
            }
        });

        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light,
                android.R.color.holo_purple);

        movies = new ArrayList<>(); //make sure to initialize before placing in adapter to prevent crashing
        movieAdapter = new MovieAdapter(this, movies);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setHasFixedSize(true);
        rvItems.setAdapter(movieAdapter);
        movieAdapter.setOnItemClickListener(new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Movie movie = movies.get(position);

                if (movie.isPopular()) {
                    Intent i = new Intent(MovieActivity.this, TrailerActivity.class);
                    i.putExtra("movieId", movie.getMovieId());
                    startActivity(i);

                } else {
                    //send movie data to DetailsActivity
                    Intent i = new Intent(MovieActivity.this, DetailsActivity.class);
                    i.putExtra("movieId", movie.getMovieId());
                    i.putExtra("movieBackdrop", movie.getBackdropPath());
                    i.putExtra("movieTitle", movie.getOriginalTitle());
                    i.putExtra("movieRating", movie.getRating());
                    i.putExtra("moviePopularity", movie.getPopularity());
                    i.putExtra("movieOverview", movie.getOverview());
                    //puts DetailsActivity on top of the stack
                    startActivity(i);
                }
            }
        });
        fetchMoviesAsync();
    }

    public void fetchMoviesAsync() {
        //initialize http client
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(nowPlayingUrl, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieResults;
                try {
                    movies.clear();
                    movieResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(movieResults));
                    movieAdapter.notifyDataSetChanged();
                    //signal refresh has finished
                    swipeRefreshLayout.setRefreshing(false);
                    Log.d("DEBUG", movies.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}
