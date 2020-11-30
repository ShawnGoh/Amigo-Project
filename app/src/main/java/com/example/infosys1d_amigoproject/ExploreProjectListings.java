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
//        getSupportActionBar().setTitle("All Projects");
        this.setTitle("All Projects");

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

    public void setTitle(String title){
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textView = new TextView(this);
        textView.setText(title);
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(textView);
    }

}