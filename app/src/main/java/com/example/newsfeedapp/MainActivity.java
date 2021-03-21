package com.example.newsfeedapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<NewsArticle>>, AdapterView.OnItemSelectedListener {

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

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(0, null, this).forceLoad();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(LOG_TAG, "onItemClick " + id);
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
        Log.i(LOG_TAG, "onCreateLoader");
        return new NewsArticleLoader(MainActivity.this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsArticle>> loader, List<NewsArticle> data) {
        // Prepare loader by either re-connecting, or creating a new one.
        getSupportLoaderManager().initLoader(0, null, this);
        adapter.setData(data);
        updateUi();;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsArticle>> loader) {
        Log.i(LOG_TAG, "onLoaderReset");
        adapter.setData(new ArrayList<>());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.i(LOG_TAG, "onItemSelected not on create");
        // Does Nothing
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Does Nothing
    }
}