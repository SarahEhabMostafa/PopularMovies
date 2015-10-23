package com.sarahehabm.popularmovies.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarahehabm.popularmovies.R;
import com.sarahehabm.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesGridAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Movie> movies;

    private final String IMAGE_SIZE = "w185";

    public MoviesGridAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies == null ? 0 : movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies == null ? null : movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return movies == null ? -1 : movies.get(position).getId();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.grid_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        Movie movie  = movies.get(position);

        viewHolder.textView.setText(movie.getTitle());
        String imageUrl = movie.constructImageURL(IMAGE_SIZE);
        Picasso.with(context).load(imageUrl).into(viewHolder.imageView);

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textView;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }
}
