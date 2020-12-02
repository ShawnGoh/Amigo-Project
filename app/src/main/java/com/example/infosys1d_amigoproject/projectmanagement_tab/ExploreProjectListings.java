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
        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.nothing, android.R.anim.fade_out);
            }
        });


        projectsList = new ArrayList<>();
        projectCategory = findViewById(R.id.category);
        firebaseMethods = new FirebaseMethods(getApplicationContext());
//        Project new_proj = new Project("random url", "test Title", "test description ",
//                new ArrayList<String>(Arrays.asList("hello","java","C++")),
//                new ArrayList<String>(Arrays.asList("3Qyanm1Rl6WgR0eB4v8oUFULth72",firebaseMethods.getUserID())),
//                firebaseMethods.getUserID());2

        // Get the intent, verify the action and get the query
        intent = getIntent();
        category_filter = intent.getStringExtra("category");
//        if (category_filter != null) {
//            System.out.println(category_filter);
//            Query query = FirebaseDatabase.getInstance().getReference("Projects").orderByChild("category").equalTo(category_filter);
//            query.addListenerForSingleValueEvent(valueEventListener);
//
//        }
//        else {
        //code for database query of projects
        dbProjects = FirebaseDatabase.getInstance().getReference("Projects");
        dbProjects.addListenerForSingleValueEvent(valueEventListener);
//        }
        System.out.println("helloooooooooooo"+projectsList.toString());

        //this block of code is for the recycler view
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //this block of code is for the button
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(),CreateNewProject.class));
            }
        });

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
            myAdapter = new MyAdapter(projectsList);
            recyclerView.setAdapter(myAdapter);
            projectsList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Project project = snapshot.getValue(Project.class);
                    projectsList.add(project);
                    System.out.println(project);
                }
                myAdapter.notifyDataSetChanged();
                System.out.println("added all items from server query");
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
                    System.out.println(skills_filter);
                    System.out.println("hello");
                    System.out.println(projectsList);
                    for (Project project : projectsList) {
                        for (String skill : skills_filter) {
                            System.out.println("Iterating through skills");
                            System.out.println(skill);
                            if (project.getSkillsrequired().contains(skill)){
                                textFilteredProjects.add(project);
                                System.out.println("Adding project with skill");
                            }
                        }
                        myAdapter.setProjectsList(textFilteredProjects);
                    }
                    System.out.println(textFilteredProjects);;
                }

                System.out.println(text_filter);
                if (text_filter != null){
                    if (text_filter.trim().length() != 0) {
                        projectCategory.setText('"'+text_filter+'"');
                    }
                }
                System.out.println("This sends data!");
                System.out.println(skills_filter);

                System.out.println(projectsList);
                myAdapter.setProjectListAll(textFilteredProjects);
                myAdapter.getFilter().filter(text_filter);

                System.out.println("Printing Item Count!");
                System.out.println(myAdapter.getItemCount());

//                while (myAdapter.getItemCount() != 0) {
//                    int i = myAdapter.getItemCount();
//                    errorMessage.setVisibility(View.VISIBLE);
//                }


            }

            else if (category_filter != null) {
                System.out.println(category_filter);
                projectCategory.setText(category_filter);
                List<Project> filteredProjects = new ArrayList<>();
                for (Project project : projectsList) {
                    if (project.getCategory().contains(category_filter)){
                        filteredProjects.add(project);
                        System.out.println("Adding project of category");
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