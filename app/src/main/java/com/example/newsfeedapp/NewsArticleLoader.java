package com.example.newsfeedapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class NewsArticleLoader extends AsyncTaskLoader<List<NewsArticle>> {

    //create log tag for error message debugging
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /** URL to query the Guardian dataset for recent news articles */
    private static String KEY = "9a4f10da-3692-459b-bc3e-aa2fb36d23a6";
    private static String REQUEST_URL = "https://content.guardianapis.com/search?api-key=9a4f10da-3692-459b-bc3e-aa2fb36d23a6";

    public NewsArticleLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsArticle> loadInBackground() {

        // Create URL object
        URL url = createUrl(REQUEST_URL);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = "";
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "" + e);
        }

        // Extract relevant fields from the JSON response and create an {@link NewsArticle} object
        ArrayList<NewsArticle> newsArticle = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
        return newsArticle;

    }

    /**
     * Returns new URL object from the given string URL.
     */
    private URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            Log.e(LOG_TAG, "" + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an {@link NewsArticle} object by parsing out information
     * about the first earthquake from the input newsJSON string.
     */
    private ArrayList<NewsArticle> extractFeatureFromJson(String newsJSON) {

        ArrayList<NewsArticle> newsList = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject featureArray = baseJsonResponse.getJSONObject("response");

            // If there are results in the features array
            int length = featureArray.length();
            for (int i = 0; i < length; i++) {
                JSONArray results = featureArray.getJSONArray("results");
                // Extract out the first feature (which is a news item)
                JSONObject articleResult = results.getJSONObject(i);

                // Extract out the title, time, and tsunami values
                String title = articleResult.getString("webTitle");
                String time = articleResult.getString("webPublicationDate");
                String webUrl = articleResult.getString("webUrl");
                String articleSection = articleResult.getString("sectionName");

                // Create a new {@link Event} object
                newsList.add(new NewsArticle(title, time, webUrl, articleSection));
                Log.i(LOG_TAG, "run iteration " + i);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return newsList;
    }

}
