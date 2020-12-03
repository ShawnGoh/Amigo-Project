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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String s1[] = {"Learn Python with Me :)", "two", "three", "four", "five"};
    String s2[] = {"one", "two", "three", "four", "five"};
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

    public DiscoverFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

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

        //   Project new_proj = new Project("random url", "test Title", "test description ","userid");

        firebaseMethods = new FirebaseMethods(container.getContext());
//        Project new_proj = new Project("random url", "test Title", "test description ",
//                new ArrayList<String>(Arrays.asList("hello","java","C++")),
//                new ArrayList<String>(Arrays.asList("3Qyanm1Rl6WgR0eB4v8oUFULth72",firebaseMethods.getUserID())),
//                firebaseMethods.getUserID());
//        MyAdapter myAdapter = new MyAdapter(new ArrayList<Project>(Arrays.asList(new_proj,new_proj)));




        searchView = view.findViewById(R.id.searchBarHome);
        searchView.setFocusable(false);
//        EditText editText = (EditText) searchView.findViewById(R.id.searchBarHome);
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
                            System.out.println("Adding PROJECT!!!!!");
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
            }
            List<Project> textFilteredProjects = new ArrayList<>();
            ArrayList<String> populatedlist = new ArrayList<>();

            if (!mUserSettings.getUsersdisplay().getSkills().isEmpty()) {
//                System.out.println(skills_filter);
                System.out.println("hello");
                System.out.println(projectsList);
                textFilteredProjects.clear();
                for (Project project : projectsList) {
                    for (String skill : mUserSettings.getUsersdisplay().getSkills()) {
                        System.out.println("Iterating through skills");
                        System.out.println(skill);
                        if (project.getSkillsrequired().contains(skill)) {
                            if(!populatedlist.contains(project.getProjectitle())) {
                                populatedlist.add(project.getProjectitle());
                                textFilteredProjects.add(project);
                                System.out.println("Adding project with skill");
                            }
                        }
                    }
                    myAdapter.setProjectsList(textFilteredProjects);
                }
                System.out.println(textFilteredProjects);


            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}