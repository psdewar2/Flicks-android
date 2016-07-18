package com.psd.flicks.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.psd.flicks.R;
import com.psd.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by PSD on 7/17/16.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    //

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get data from position
        Movie movie = getItem(position);

        //check the existing view being reused
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
        }

        //find the image view
        ImageView movieImageView = (ImageView) convertView.findViewById(R.id.movieImageView);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);

        //clear out image from convertView
        movieImageView.setImageResource(0);

        //populate data
        String path = movie.getPosterPath();
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            path = movie.getBackdropPath();
        }
        Picasso.with(getContext()).load(path).into(movieImageView);
        tvTitle.setText(movie.getOriginalTitle());
        tvOverview.setText(movie.getOverview());

        return convertView;
    }
}
