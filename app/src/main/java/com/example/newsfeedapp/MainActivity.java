package com.example.newsfeedapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    ArrayList<NewsArticle> newsList = new ArrayList<>();

    /** URL to query the Guardian dataset for recent news articles */
    private static String KEY = "9a4f10da-3692-459b-bc3e-aa2fb36d23a6";
    private static String REQUEST_URL = "https://content.guardianapis.com/search?api-key=9a4f10da-3692-459b-bc3e-aa2fb36d23a6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kick off an {@link AsyncTask} to perform the network request
        NewsAsyncTask task = new NewsAsyncTask();
        task.execute();

        ListView listView = findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsArticle selection = newsList.get(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selection.getWebUrl()));
                startActivity(browserIntent);
            }

        });

    }

    /**
     * Update the screen to display information from the given {@link NewsArticle}.
     */
    private void updateUi() {
        // Display the earthquake title in the UI
        NewsCustomAdapter adapter = new NewsCustomAdapter(this, newsList);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        Log.i(LOG_TAG, "updateUi call");

    }

    /**
     * Open item link on item selection
     * @param position position of selected item
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Does Nothing
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //Does Nothing
    }

    /**
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the first earthquake in the response.
     */
    private class NewsAsyncTask extends AsyncTask<URL, Void, NewsArticle> {

        @Override
        protected NewsArticle doInBackground(URL... urls) {
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
            NewsArticle newsArticle = extractFeatureFromJson(jsonResponse);

            // Return the {@link Event} object as the result fo the {@link TsunamiAsyncTask}
            return newsArticle;
        }

        /**
         * Update the screen with the given newsArticle (which was the result of the
         * {@link NewsAsyncTask}).
         */
        @Override
        protected void onPostExecute(NewsArticle newsArticle) {
            if (newsList == null) {
                Log.e(LOG_TAG, "ERROR: newsArticle == null");
                return;
            }
            Log.i(LOG_TAG, "onPostExecute updating UI");
            updateUi();
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
        private NewsArticle extractFeatureFromJson(String newsJSON) {
            try {
                JSONObject baseJsonResponse = new JSONObject(newsJSON);
                JSONObject featureArray = baseJsonResponse.getJSONObject("response");

                // If there are results in the features array
                int length = featureArray.length();
                for (int i = 0; i < length; i++) {
                    JSONArray results = featureArray.getJSONArray("results");
                    // Extract out the first feature (which is a news item)
                    JSONObject firstResult = results.getJSONObject(i);

                    // Extract out the title, time, and tsunami values
                    String title = firstResult.getString("webTitle");
                    String time = firstResult.getString("webPublicationDate");
                    String webUrl = firstResult.getString("webUrl");

                    // Create a new {@link Event} object
                    newsList.add(new NewsArticle(title, time, webUrl));
                    Log.i(LOG_TAG, "run iteration " + i);
                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
            }
            return null;
        }
    }
}