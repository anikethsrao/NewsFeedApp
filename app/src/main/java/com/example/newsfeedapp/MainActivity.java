package com.example.newsfeedapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<NewsArticle>>, AdapterView.OnItemSelectedListener {

    //create log tag for error message debugging
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private NewsCustomAdapter adapter;
    ArrayList<NewsArticle> newsList = new ArrayList<>();
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = new NewsCustomAdapter(this, new ArrayList<>());
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);


        emptyView = findViewById(R.id.empty_list);
        listView.setEmptyView(emptyView);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected){
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(0, null, this).forceLoad();
        } else {
            emptyView.setText(R.string.no_internet);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(LOG_TAG, "onItemClick " + id);
                NewsArticle selection = newsList.get(position);
                Log.i(LOG_TAG, selection.toString());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(selection.webUrl));
                startActivity(browserIntent);
            }
        });
    }

    @NonNull
    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i(LOG_TAG, "onCreateLoader");
        return new NewsArticleLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsArticle>> loader, List<NewsArticle> data) {
        Log.i(LOG_TAG, "onLoadFinished");
        adapter.setData(data);
        newsList = new ArrayList<>(data);
        // Set empty state text to display "No earthquakes found."
        emptyView.setText(R.string.no_news);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsArticle>> loader) {
        Log.i(LOG_TAG, "onLoaderReset");
        adapter.setData(new ArrayList<>());
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