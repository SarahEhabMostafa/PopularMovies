package com.sarahehabm.popularmovies.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sarahehabm.popularmovies.R;
import com.sarahehabm.popularmovies.model.MovieTrailer;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sarah E. Mostafa on 12-Dec-15.
 */
public class MovieTrailersListAdapter extends ArrayAdapter<MovieTrailer> {
    private static final String TAG = MovieTrailersListAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<MovieTrailer> movieTrailers;

    public MovieTrailersListAdapter(Context context, ArrayList<MovieTrailer> movieTrailers) {
        super(context, R.layout.list_item_movie_trailer);

        this.context = context;
        this.movieTrailers = movieTrailers;
    }

    @Override
    public int getCount() {
        return movieTrailers == null? 0 : movieTrailers.size();
    }

    @Override
    public MovieTrailer getItem(int position) {
        return (movieTrailers == null || movieTrailers.size()==0)?
                null : movieTrailers.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item_movie_trailer, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        if(movieTrailers!=null) {
            final MovieTrailer movieTrailer = movieTrailers.get(position);
            viewHolder.textViewTitle.setText(movieTrailer.getName());
            viewHolder.imageButtonPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://www.youtube.com/watch?v=" + movieTrailer.getKey();
                    Log.i(TAG, url);
                    Toast.makeText(context, url, Toast.LENGTH_SHORT).show();


                    Intent openTrailerIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("vnd.youtube://" + movieTrailer.getKey()));
                    if(openTrailerIntent.resolveActivity(context.getPackageManager()) != null)
                        context.startActivity(openTrailerIntent);
                    else {
                        openTrailerIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse(url));
                        context.startActivity(openTrailerIntent);
                    }
                }
            });
        } else
            Toast.makeText(context, "Null list!", Toast.LENGTH_SHORT).show();

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.imageButton_play) ImageButton imageButtonPlay;
        @Bind(R.id.textView_trailer_title) TextView textViewTitle;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void  setMovieTrailers(ArrayList<MovieTrailer> movieTrailers) {
        this.movieTrailers = movieTrailers;
    }
}
