package com.sarahehabm.popularmovies.controller;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sarahehabm.popularmovies.Constants;
import com.sarahehabm.popularmovies.R;
import com.sarahehabm.popularmovies.model.MovieTrailer;
import com.sarahehabm.popularmovies.model.MovieTrailerCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sarah E. Mostafa on 12-Dec-15.
 */
public class GetMovieTrailersTask extends AsyncTask<String, Void, ArrayList<MovieTrailer>> {
    private final String TAG = GetMovieTrailersTask.class.getSimpleName();

    private Context context;
    private String movieId;
    private OnRetrieveListener onRetrieveListener;
    private OnMovieTrailersRetrievedListener onMovieTrailersRetrievedListener;


    public GetMovieTrailersTask(Context context, OnRetrieveListener onRetrieveListener,
                                OnMovieTrailersRetrievedListener
                                        onMovieTrailersRetrievedListener) {
        this.context = context;
        this.onRetrieveListener = onRetrieveListener;
        this.onMovieTrailersRetrievedListener = onMovieTrailersRetrievedListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onRetrieveListener.showProgress();
    }

    @Override
    protected ArrayList<MovieTrailer> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String responseJson = null;

        String apiValue = context.getResources().getString(R.string.api_key);
        if(params.length == 0)
            return null;

        movieId = params[0];

        try {
            Uri builtUri = Uri.parse(Constants.BASE_URL_MOVIE_TRAILERS).buildUpon()
                    .appendPath(movieId)
                    .appendPath("videos")
                    .appendQueryParameter(Constants.API_KEY, apiValue).build();

            URL requestURL = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            if(inputStream == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null)
                stringBuffer.append(line + "\n");

            if(stringBuffer.length() == 0)
                return null;

            responseJson = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error!", e);
            return null;
        } finally {
            if(urlConnection != null)
                urlConnection.disconnect();

            if(reader!=null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        MovieTrailerCollection movieTrailerCollection = MovieTrailerCollection
                .initFromJSON(responseJson);
        return (ArrayList<MovieTrailer>) movieTrailerCollection.getResults();
    }

    @Override
    protected void onPostExecute(ArrayList<MovieTrailer> movieTrailers) {
        super.onPostExecute(movieTrailers);
        onRetrieveListener.hideProgress();

        if(movieTrailers == null)
            onMovieTrailersRetrievedListener.onRetrieveFailure();
        else
            onMovieTrailersRetrievedListener.onRetrieveSuccess(movieTrailers);
    }
}
