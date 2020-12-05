package com.example.infosys1d_amigoproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.adapter.MyAdapter;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.projectmanagement_tab.ExploreProjectListings;
import com.example.infosys1d_amigoproject.projectmanagement_tab.Project;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button seeAllButton;
    private SearchView searchView;
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    FirebaseMethods firebaseMethods;
    DatabaseReference databaseReference;
    DatabaseReference dbProjects;
    public List<Project> projectsList;
    private Userdataretrieval mUserSettings;
    private FirebaseAuth mAuth;
    private String userID;

    private ImageButton learnButton;
    private ImageButton softwareButton;
    private ImageButton hardwareButton;
    private ImageButton startupButton;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater inflater2 = LayoutInflater.from(container.getContext());
        View view = inflater2.inflate(R.layout.fragment_discover, container, false);
        dbProjects = FirebaseDatabase.getInstance().getReference("Projects");
        dbProjects.addListenerForSingleValueEvent(valueEventListener);

        recyclerView = view.findViewById(R.id.suggestedRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        seeAllButton = view.findViewById(R.id.seeAllButton1);
        seeAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(container.getContext(), ExploreProjectListings.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, R.anim.nothing);

            }
        });

        learnButton = view.findViewById(R.id.projecticon1);
        learnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExploreProjectListings.class);
                intent.putExtra("category", "Learn");
                startActivity(intent);
            }
        });
        hardwareButton = view.findViewById(R.id.projecticon2);
        hardwareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExploreProjectListings.class);
                intent.putExtra("category", "Hardware");
                startActivity(intent);
            }
        });
        softwareButton = view.findViewById(R.id.projecticon3);
        softwareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExploreProjectListings.class);
                intent.putExtra("category", "Software");
                startActivity(intent);
            }
        });
        startupButton = view.findViewById(R.id.projecticon4);
        startupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExploreProjectListings.class);
                intent.putExtra("category", "Start-Up");
                startActivity(intent);
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addListenerForSingleValueEvent(valueEventListener);
//        DatabaseReference projref = databaseReference.child("Projects");

        projectsList = new ArrayList<>();

        firebaseMethods = new FirebaseMethods(container.getContext());



        searchView = view.findViewById(R.id.searchBarHome);
        searchView.setFocusable(false);
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    SearchFragment searchFragment = new SearchFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, searchFragment);
                    transaction.commit();
                    MainActivity.menu_bottom.setVisibility(View.GONE);
                }
            }
        });
        return view;
    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            mUserSettings = firebaseMethods.getUserData(dataSnapshot);
            myAdapter = new MyAdapter(projectsList);
            recyclerView.setAdapter(myAdapter);
            projectsList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals("Projects")){
                        for (DataSnapshot snapsnapshot : snapshot.getChildren()) {
                            Project project = snapsnapshot.getValue(Project.class);
                            if(!projectsList.contains(project)){
                            projectsList.add(project);}
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
            List<Project> textFilteredProjects = new ArrayList<>();
            ArrayList<String> populatedlist = new ArrayList<>();

            if (!mUserSettings.getUsersdisplay().getSkills().isEmpty()) {
                textFilteredProjects.clear();
                for (Project project : projectsList) {
                    for (String skill : mUserSettings.getUsersdisplay().getSkills()) {
                        if (project.getSkillsrequired().contains(skill)) {
                            if(!populatedlist.contains(project.getProjectitle())) {
                                populatedlist.add(project.getProjectitle());
                                textFilteredProjects.add(project);
                            }
                        }
                    }
                    myAdapter.setProjectsList(textFilteredProjects);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}