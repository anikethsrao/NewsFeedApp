package com.example.newsfeedapp;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsCustomAdapter extends ArrayAdapter<NewsArticle> {


    public NewsCustomAdapter(@NonNull Context context, @NonNull List<NewsArticle> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listView = convertView;
        if (listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.news_layout, parent,false);
        }

        NewsArticle currentItem = getItem(position);

        TextView articleTitle = listView.findViewById(R.id.title);
        articleTitle.setText(currentItem.getTitle());

        TextView articlePublishTime = listView.findViewById(R.id.date);
        articlePublishTime.setText(currentItem.getPublishTime());

        return listView;

    }
}
