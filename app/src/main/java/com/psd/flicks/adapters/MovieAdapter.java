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

    //item_popular.xml
    public static class PopularViewHolder {
        ImageView movieBackDropImageView;
    }

    //item_movie.xml
    public static class MovieViewHolder {
        ImageView movieImageView;
        TextView tvTitle;
        TextView tvOverview;
    }

    //constructor
    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    //returns an integer representing the type: 1 if movie is popular, 0 if not
    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isPopular()) return 1;
        return 0; //return 0 if movie is not popular
    }

    //returns total number of types
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get movie object from position
        Movie movie = getItem(position);
        //get data item type for this position
        int type = getItemViewType(position);

        //Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            //based on type return the correct inflated XML layout file
            if (type == 1) {
                PopularViewHolder pVH = new PopularViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_popular, parent, false);
                pVH.movieBackDropImageView = (ImageView) convertView.findViewById(R.id.movieBackDropImageView);
                convertView.setTag(pVH);
            } else {
                MovieViewHolder mVH = new MovieViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_movie, parent, false);
                mVH.movieImageView = (ImageView) convertView.findViewById(R.id.movieImageView);
                mVH.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
                mVH.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
                convertView.setTag(mVH);
            }
        } else {
            if (type == 1) {
                PopularViewHolder pVH = (PopularViewHolder) convertView.getTag();
                Picasso.with(getContext()).load(movie.getBackdropPath()).placeholder(R.mipmap.ic_launcher).into(pVH.movieBackDropImageView);
            } else {
                MovieViewHolder mVH = (MovieViewHolder) convertView.getTag();
                //clear out image from convertView
                mVH.movieImageView.setImageResource(0);

                //populate data into the template view using the data object
                String path = movie.getPosterPath();
                int orientation = getContext().getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    path = movie.getBackdropPath();
                }
                Picasso.with(getContext()).load(path).placeholder(R.mipmap.ic_launcher).into(mVH.movieImageView);
                mVH.tvTitle.setText(movie.getOriginalTitle());
                mVH.tvOverview.setText(movie.getOverview());
            }
        }

        //return the completed view to render on screen
        return convertView;
    }
}
