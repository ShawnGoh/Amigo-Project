package com.example.infosys1d_amigoproject.projectmanagement_tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.MyAdapter;
import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.models.users_display;
import com.example.infosys1d_amigoproject.models.users_private;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class MyProjectsFragment extends Fragment {

    MyAdapter myAdapter;
    private TextView mTitleName;
    private static final String TAG = "profilefragment";
    //Firebase Database
    private FirebaseDatabase mFirebasedatabase;
    private DatabaseReference databaseReference;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;
    private FirebaseMethods firebaseMethods;
    users_display user;

    RecyclerView recyclerView;
    public MyProjectsFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_projects, container, false);
        mTitleName = view.findViewById(R.id.Title);
        recyclerView = view.findViewById(R.id.suggestedRecycler2);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        setupfirebaseauth();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mref2 = FirebaseDatabase.getInstance().getReference("users_display").child(FirebaseAuth.getInstance().getUid());
        mref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user= snapshot.getValue(users_display.class);
                mTitleName.setText(user.getName() + "'s Projects");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference projref = databaseReference.child("Projects");
        ArrayList<Project> projectList = new ArrayList<>();
        myAdapter = new MyAdapter(projectList);
        recyclerView.setAdapter(myAdapter);



        projref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                projectList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Project project = postSnapshot.getValue(Project.class);
                    for (String userID: project.getUsersinProject()){
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid() == (userID)){
                            projectList.add(project);
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: ");
            }
        });
        return view;
    }




    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthstatelistner);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthstatelistner!=null){
            mAuth.removeAuthStateListener(mAuthstatelistner);
        }
    }

    //FirebaseAuth
    private void setupfirebaseauth(){
        Log.d(TAG, "Setup FirebaseAuth");

        mAuth = FirebaseAuth.getInstance();
        mFirebasedatabase = FirebaseDatabase.getInstance();
        databaseReference = mFirebasedatabase.getReference();;

        //check if user is sign in
        mAuthstatelistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user  = firebaseAuth.getCurrentUser();


                if(user !=null){
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in" +user.getUid());
                }
                else{
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };


    }
}