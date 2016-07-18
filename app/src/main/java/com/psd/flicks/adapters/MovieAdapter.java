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

    //View lookup cache
    public static class ViewHolder {
        ImageView movieImageView;
        TextView tvTitle;
        TextView tvOverview;
    }

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, R.layout.item_movie, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get data from position
        Movie movie = getItem(position);

        //Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; //view lookup cache stored in tag

        //check the existing view being reused
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder.movieImageView = (ImageView) convertView.findViewById(R.id.movieImageView);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //clear out image from convertView
        viewHolder.movieImageView.setImageResource(0);

        //populate data into the template view using the data object
        String path = movie.getPosterPath();
        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            path = movie.getBackdropPath();
        }
        Picasso.with(getContext()).load(path).into(viewHolder.movieImageView);
        viewHolder.tvTitle.setText(movie.getOriginalTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        //return the completed view to render on screen
        return convertView;
    }
}
