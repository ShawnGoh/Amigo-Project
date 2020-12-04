package com.example.infosys1d_amigoproject.projectmanagement_tab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.adapter.MyAdapter;
import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExploreProjectListings extends AppCompatActivity {


    FirebaseMethods firebaseMethods;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    DatabaseReference dbProjects;
    public List<Project> projectsList;
    private Intent intent;
    MyAdapter myAdapter;
    TextView projectCategory;
    String category_filter;
    ImageButton closebutton;
    TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_project_listings);

        errorMessage = findViewById(R.id.errorMessage);
        closebutton = findViewById(R.id.allprojectsclosebutton);
        projectCategory = findViewById(R.id.category);
        floatingActionButton = findViewById(R.id.floatingActionButton);


        projectsList = new ArrayList<>();
        firebaseMethods = new FirebaseMethods(getApplicationContext());

        // Get the intent, verify the action and get the query
        intent = getIntent();
        category_filter = intent.getStringExtra("category");


        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.nothing, android.R.anim.fade_out);
            }
        });



        //code for database query of projects
        dbProjects = FirebaseDatabase.getInstance().getReference("Projects");
        dbProjects.addListenerForSingleValueEvent(valueEventListener);

        //this block of code is for the recycler view
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(view.getContext(),CreateNewProject.class), 101);
            }
        });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            myAdapter = new MyAdapter(projectsList);
            recyclerView.setAdapter(myAdapter);
            projectsList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Project project = snapshot.getValue(Project.class);
                    projectsList.add(project);
                }
                myAdapter.notifyDataSetChanged();
            }
            myAdapter.setProjectListAll(projectsList);
            if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
                ArrayList<String> skills_filter = intent.getStringArrayListExtra("skills_filter");
                String text_filter = intent.getStringExtra("text_filter");
                List<Project> textFilteredProjects = new ArrayList<>();

                // ORDER IS IMPORTANT SINCE ITS ASYNC

                if (skills_filter != null && skills_filter.size() != 0) {
                    if (text_filter == null) {
                        projectCategory.setText("Search results");
                    }
                    for (Project project : projectsList) {
                        for (String skill : skills_filter) {
                            if (project.getSkillsrequired().contains(skill)){
                                textFilteredProjects.add(project);
                            }
                        }
                        myAdapter.setProjectsList(textFilteredProjects);
                    }
                }
                else {
                    textFilteredProjects = projectsList;
                }

                if (text_filter != null){
                    if (text_filter.trim().length() != 0) {
                        projectCategory.setText('"'+text_filter+'"');
                    }
                }

                myAdapter.setProjectListAll(textFilteredProjects);
                myAdapter.getFilter().filter(text_filter);
            }

            else if (category_filter != null) {
                projectCategory.setText(category_filter);
                List<Project> filteredProjects = new ArrayList<>();
                for (Project project : projectsList) {
                    if (project.getCategory().contains(category_filter)){
                        filteredProjects.add(project);
                    }
                }
                myAdapter.setProjectsList(filteredProjects);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };



}