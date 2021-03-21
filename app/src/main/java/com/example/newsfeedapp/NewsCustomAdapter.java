package com.example.newsfeedapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NewsCustomAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<NewsArticle> newsArticles = new ArrayList<NewsArticle>();

    public NewsCustomAdapter(Context context, List<NewsArticle> newsArticles) {
        this.newsArticles = newsArticles;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public int getCount() {
        return newsArticles.size();
    }

    @Override
    public Object getItem(int position) {
        return newsArticles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootView = convertView;
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.news_layout, parent, false);
        }

        NewsArticle currentItem = (NewsArticle) getItem(position);

        TextView titleText = rootView.findViewById(R.id.title);
        titleText.setText(currentItem.title);

        TextView publishDate = rootView.findViewById(R.id.date);
        publishDate.setText(currentItem.publishTime);

        TextView articleSection = rootView.findViewById(R.id.articleSection);
        articleSection.setText(currentItem.articleSection);

        return rootView;

    }

    public void setData(List<NewsArticle> data) {
        newsArticles.addAll(data);
        notifyDataSetChanged();
    }
}
