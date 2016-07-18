package com.psd.flicks.models;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by PSD on 7/17/16.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }
}
