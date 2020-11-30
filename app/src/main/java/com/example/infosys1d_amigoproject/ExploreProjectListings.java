package com.example.infosys1d_amigoproject;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ExploreProjectListings extends AppCompatActivity {
    ListView projectListView;
    List projectList = new ArrayList();
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_project_listings);

        // need to change such that it loads Firebase data
        projectListView = findViewById(R.id.suggestedListView);

        projectList.add("Orange");
        projectList.add("123");
        projectList.add("456");
        projectList.add("789");
        projectList.add("2343e");
        projectList.add("2343e");
        projectList.add("2sdsa");
        projectList.add("23dsfdsfe");

        adapter = new ArrayAdapter(com.example.infosys1d_amigoproject.ExploreProjectListings.this,android.R.layout.simple_list_item_1,projectList);
        projectListView.setAdapter(adapter);
    }



}