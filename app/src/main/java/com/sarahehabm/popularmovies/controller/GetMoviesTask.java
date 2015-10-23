package com.sarahehabm.popularmovies.controller;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.sarahehabm.popularmovies.Constants;
import com.sarahehabm.popularmovies.R;
import com.sarahehabm.popularmovies.model.Movie;
import com.sarahehabm.popularmovies.model.MovieCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Sarah E. Mostafa on 14-Oct-15.
 */
public class GetMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
    private final String TAG = GetMoviesTask.class.getSimpleName();

    private Context context;
    private OnRetrieveListener onRetrieveListener;
    private OnDataRetrieveSuccessListener onDataRetrieveSuccessListener;

    public GetMoviesTask(Context context, OnRetrieveListener onRetrieveListener, OnDataRetrieveSuccessListener onDataRetrieveSuccessListener) {
        this.context = context;
        this.onRetrieveListener = onRetrieveListener;
        this.onDataRetrieveSuccessListener = onDataRetrieveSuccessListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        onRetrieveListener.showProgress();
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String responseJson = null;

        String apiValue = context.getResources().getString(R.string.api_key);
        String sortValue = context.getString(R.string.preference_default_sort_order);
        if(params.length != 0)
            sortValue = params[0];

        try {
            Uri builtUri = Uri.parse(Constants.BASE_URL).buildUpon()
                    .appendQueryParameter(Constants.SORT_KEY, sortValue)
                    .appendQueryParameter(Constants.API_KEY, apiValue).build();

            URL requestURL = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer stringBuffer = new StringBuffer();
            if (inputStream == null)
                return null;

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            if (stringBuffer.length() == 0)
                return null;

            responseJson = stringBuffer.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error!", e);
            return null;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }
        MovieCollection movieCollection = MovieCollection.initFromJSON(responseJson);
        return (ArrayList<Movie>)movieCollection.getResults();
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        onRetrieveListener.hideProgress();
        onDataRetrieveSuccessListener.updateUI(movies);
    }
}
