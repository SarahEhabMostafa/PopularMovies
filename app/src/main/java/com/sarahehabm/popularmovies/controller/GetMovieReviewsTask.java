package com.sarahehabm.popularmovies.controller;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sarahehabm.popularmovies.Constants;
import com.sarahehabm.popularmovies.R;
import com.sarahehabm.popularmovies.model.MovieRating;
import com.sarahehabm.popularmovies.model.MovieRatingCollection;

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
public class GetMovieReviewsTask extends AsyncTask<String, Void, ArrayList<MovieRating>> {
    private final String TAG = GetMovieReviewsTask.class.getSimpleName();

    private String movieId;
    private Context context;
    private OnRetrieveListener onRetrieveListener;
    private OnMovieRatingsRetrievedListener onMovieRatingsRetrievedListener;

    public GetMovieReviewsTask(Context context, OnRetrieveListener onRetrieveListener,
                               OnMovieRatingsRetrievedListener onMovieRatingsRetrievedListener) {
        this.context = context;
        this.onRetrieveListener = onRetrieveListener;
        this.onMovieRatingsRetrievedListener = onMovieRatingsRetrievedListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onRetrieveListener.showProgress();
    }

    @Override
    protected ArrayList<MovieRating> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        String responseJson = null;

        String apiValue = context.getString(R.string.api_key);
        if(params.length == 0)
            return null;

        movieId = params[0];

        try {
            Uri builtUri = Uri.parse(Constants.BASE_URL_MOVIE_RATINGS).buildUpon()
                    .appendPath(movieId)
                    .appendPath("reviews")
                    .appendQueryParameter(Constants.API_KEY, apiValue).build();

            URL requestURL = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            if(inputStream == null)
                return null;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine())!=null)
                stringBuffer.append(line + "\n");

            if(stringBuffer.length() == 0)
                return null;

            responseJson = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error!", e);
            return null;
        } finally {
            if(urlConnection!=null)
                urlConnection.disconnect();

            if(bufferedReader!=null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        MovieRatingCollection movieRatingCollection = MovieRatingCollection
                .initFromJSON(responseJson);
        return (ArrayList<MovieRating>) movieRatingCollection.getResults();
    }

    @Override
    protected void onPostExecute(ArrayList<MovieRating> movieRatings) {
        super.onPostExecute(movieRatings);
        onRetrieveListener.hideProgress();

        if(movieRatings == null)
            onMovieRatingsRetrievedListener.onRetrieveFailure();
        else
            onMovieRatingsRetrievedListener.onRetrieveSuccess(movieRatings);
    }
}
