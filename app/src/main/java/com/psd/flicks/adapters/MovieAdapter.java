package com.psd.flicks.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psd.flicks.R;
import com.psd.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by PSD on 7/17/16.
 */
public class MovieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Movie> movieList;

    // Store the context for easy access
    private Context mContext;

    //listener member variables
    private static OnItemClickListener mClickListener;

    // Define the listener interface for normal/long clicks
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mClickListener = listener;
    }

    //item_popular.xml
    public static class PopularViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movieBackDropImageView) ImageView movieBackDropImageView;
        public PopularViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getLayoutPosition());
                    }
                }
            });
            ButterKnife.bind(this, view);
        }
    }

    //item_movie.xml
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movieImageView) ImageView movieImageView;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;
        public MovieViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null) {
                        mClickListener.onItemClick(v, getLayoutPosition());
                    }
                }
            });
            ButterKnife.bind(this, view);
        }
    }

    //constructor
    public MovieAdapter(Context context, List<Movie> movies) {
        mContext = context;
        movieList = movies;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    //returns an integer representing the type: 1 if movie is popular, 0 if not
    @Override
    public int getItemViewType(int position) {
        if (movieList.get(position).isPopular()) return 1;
        return 0; //return 0 if movie is not popular
    }

    //creates different RecyclerView.ViewHolder objects based on the item view type
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int itemViewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (itemViewType) {
            case 1:
                viewHolder = new PopularViewHolder(inflater.inflate(R.layout.item_popular, parent, false));
                break;
            default:
                viewHolder = new MovieViewHolder(inflater.inflate(R.layout.item_movie, parent, false));
                break;
        }
        return viewHolder;
    }

    //updates the RecyclerView.ViewHolder contents with the item at the given position
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        Movie movie = movieList.get(position);
        switch (viewHolder.getItemViewType()) {
            case 1:
                PopularViewHolder pVH = (PopularViewHolder) viewHolder;
                Picasso.with(getContext()).load(movie.getBackdropPath()).transform(new RoundedCornersTransformation(10,10))
                        .placeholder(R.mipmap.ic_launcher).into(pVH.movieBackDropImageView);
                break;
            default:
                MovieViewHolder mVH = (MovieViewHolder) viewHolder;
                //clear out image from convertView
                mVH.movieImageView.setImageResource(0);
                //populate data into the template view using the data object
                String path = movie.getPosterPath();
                int orientation = getContext().getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    path = movie.getBackdropPath();
                }
                Picasso.with(getContext()).load(path).transform(new RoundedCornersTransformation(10,10))
                        .placeholder(R.mipmap.ic_launcher).into(mVH.movieImageView);
                mVH.tvTitle.setText(movie.getOriginalTitle());
                mVH.tvOverview.setText(movie.getOverview());
                break;
        }
    }
}
