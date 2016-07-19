package com.psd.flicks.controllers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.psd.flicks.R;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {
    ImageView backdropImageView;
    TextView tvDetailsTitle, tvDetailsPopularity, tvDetailsOverview;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //get intent data
        String movieBackdrop = getIntent().getStringExtra("movieBackdrop");
        String movieTitle = getIntent().getStringExtra("movieTitle");
        double movieRating = getIntent().getDoubleExtra("movieRating", 5.0);
        double moviePopularity = getIntent().getDoubleExtra("moviePopularity", 1.0);
        String movieOverview = getIntent().getStringExtra("movieOverview");

        //widgets
        backdropImageView = (ImageView) findViewById(R.id.detailsBackdropImageView);
        tvDetailsTitle = (TextView) findViewById(R.id.tvDetailsTitle);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        tvDetailsPopularity = (TextView) findViewById(R.id.tvDetailsPopularity);
        tvDetailsOverview = (TextView) findViewById(R.id.tvDetailsOverview);

        Picasso.with(this).load(movieBackdrop).placeholder(R.mipmap.ic_launcher).into(backdropImageView);
        tvDetailsTitle.setText(movieTitle);
        ratingBar.setRating((float) (movieRating));
        tvDetailsPopularity.setText(String.valueOf("Popularity: " + moviePopularity));
        tvDetailsOverview.setText(movieOverview);

    }
}
