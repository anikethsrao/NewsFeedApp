package com.example.newsfeedapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.HttpAuthHandler;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String KEY = "9a4f10da-3692-459b-bc3e-aa2fb36d23a6";
    private static String accessURL = "https://content.guardianapis.com/search?api-key=9a4f10da-3692-459b-bc3e-aa2fb36d23a6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}