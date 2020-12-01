package com.example.infosys1d_amigoproject.projectmanagement;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.MyAdapter;
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
import java.util.Arrays;
import java.util.List;

public class ExploreProjectListings extends AppCompatActivity {


    FirebaseMethods firebaseMethods;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    DatabaseReference dbProjects;
    public List<Project> projectsList;
    private Intent intent;
    private Context mContext;
    MyAdapter myAdapter;
    TextView projectCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_project_listings);
        projectsList = new ArrayList<>();
        projectCategory = findViewById(R.id.category);
        firebaseMethods = new FirebaseMethods(getApplicationContext());
//        Project new_proj = new Project("random url", "test Title", "test description ",
//                new ArrayList<String>(Arrays.asList("hello","java","C++")),
//                new ArrayList<String>(Arrays.asList("3Qyanm1Rl6WgR0eB4v8oUFULth72",firebaseMethods.getUserID())),
//                firebaseMethods.getUserID());

        //code for database query of projects
        dbProjects = FirebaseDatabase.getInstance().getReference("Projects");
        dbProjects.addListenerForSingleValueEvent(valueEventListener);
        System.out.println("helloooooooooooo"+projectsList.toString());

        //this block of code is for the button
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),CreateNewProject.class));
            }
        });
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Get the intent, verify the action and get the query
        intent = getIntent();
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String skills_filter = intent.getStringExtra("skills_filter");
//            String text_filter = intent.getStringExtra("text_filter");
//            System.out.println("This sends data!");
//            System.out.println(skills_filter);
//
//            projectsList = new ArrayList<>();
//            myAdapter = new MyAdapter(projectsList);
//            System.out.println(projectsList);
//            dbProjects = FirebaseDatabase.getInstance().getReference("Projects");
//            dbProjects.addListenerForSingleValueEvent(valueEventListener);
//            recyclerView = findViewById(R.id.recycler);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            recyclerView.setAdapter(myAdapter);
//
//
//            projectCategory.setText(text_filter);
//            // first, query the database projects data by skills required
////            Query querySkills = FirebaseDatabase.getInstance().getReference("Projects")
////                    .orderByChild("skillsrequired").equalTo();
//
//
//            myAdapter.getFilter().filter(text_filter);
//        }
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //this block of code is for the recycler view
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
                String skills_filter = intent.getStringExtra("skills_filter");
                String text_filter = intent.getStringExtra("text_filter");
                projectCategory.setText('"'+text_filter+'"');
                System.out.println("This sends data!");
                System.out.println(skills_filter);

                System.out.println(projectsList);
                myAdapter.getFilter().filter(text_filter);

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };



}