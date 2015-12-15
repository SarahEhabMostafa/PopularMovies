package com.sarahehabm.popularmovies.view;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarahehabm.popularmovies.R;
import com.sarahehabm.popularmovies.controller.database.MovieContract.MovieEntry;
import com.sarahehabm.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoviesGridAdapter extends CursorAdapter {
    private final int NUM_VIEW_TYPES = 1;
    private final int VIEW_TYPE_NORMAL = 1;

    private Context context;

    private static final String IMAGE_SIZE = "w185";

    public MoviesGridAdapter(Context context, Cursor c, int flags, ArrayList<Movie> movies) {
        super(context, c, flags);
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return NUM_VIEW_TYPES;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                layoutId = R.layout.grid_item;
                break;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int movieId = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_ID));
        String movieTitle = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
        String movieImagePath = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
        String imageUrl = Movie.constructImageURL(movieImagePath, IMAGE_SIZE);

        viewHolder.textView.setText(movieTitle);
        Picasso.with(context)
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(viewHolder.imageView);
    }

    static class ViewHolder {
        @Bind(R.id.imageView) ImageView imageView;
        @Bind(R.id.textView) TextView textView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setMovies(ArrayList<Movie> movies) {
    }
}
