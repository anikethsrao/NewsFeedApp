package com.example.newsfeedapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

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
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArticle>>, AdapterView.OnItemSelectedListener {
    //TODO: Refractor to use loaders

    //create log tag for error message debugging
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private NewsCustomAdapter adapter;

    ArrayList<NewsArticle> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new NewsCustomAdapter(this, new ArrayList<>());
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        LoaderManager.getInstance(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsArticle selection = newsList.get(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selection.webUrl));
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

    @NonNull
    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsArticleLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsArticle>> loader, List<NewsArticle> data) {
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsArticle>> loader) {
        adapter.setData(new ArrayList<NewsArticle>());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Does Nothing
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Does Nothing
    }
}