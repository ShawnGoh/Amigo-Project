package com.example.infosys1d_amigoproject.projectmanagement;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.MyAdapter;
import com.example.infosys1d_amigoproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ExploreProjectListings extends AppCompatActivity {
    String s1[] = {"one", "two", "three", "four", "five"};
    String s2[] = {"one", "two", "three", "four", "five"};

    Project sampleProject = new Project(null, "Learn Python with Me!", "Hi, I'm Kai Feng", s1, s2, "Kai Feng");


    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_project_listings);

        //this block of code is for the recycler view
        MyAdapter myAdapter = new MyAdapter(sampleProject);
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //this block of code is for the button
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),CreateNewProject.class));
            }
        });
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String skills_filter = intent.getStringExtra("skills_filter");
            String text_filter = intent.getStringExtra("text_filter");
            myAdapter.getFilter().filter(text_filter);

        }
    }



}