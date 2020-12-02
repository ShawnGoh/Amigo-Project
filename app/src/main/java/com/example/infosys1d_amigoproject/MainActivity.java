package com.example.infosys1d_amigoproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.chat_tab.ChatsFragment;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.profilemanagement_tab.profilefragment;
import com.example.infosys1d_amigoproject.profilesetup.ProfileSetupAboutMe;
import com.example.infosys1d_amigoproject.projectmanagement_tab.ExploreProjectListings;
import com.example.infosys1d_amigoproject.projectmanagement_tab.MyProjectsFragment;
import com.example.infosys1d_amigoproject.signinsignup.SignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.graphics.Typeface;
import android.view.Gravity;

import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // for testing purposes, Main Activity file should be compiled after every view has been completed.

    private static final String TAG = "MainActivity/Homescreen";
    Button profilepagebutton;
    FirebaseMethods firebaseMethod = new FirebaseMethods(MainActivity.this);
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;
    Userdataretrieval user;
    ListView suggestedListView;
    List suggestedList = new ArrayList();
    ArrayAdapter adapter;
    static ChipNavigationBar menu_bottom;
    FragmentManager fragmentManager;

    RecyclerView recyclerView;

    DatabaseReference myref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // use this to switch between activity views
        //this.setTitle("Explore");
        myref = FirebaseDatabase.getInstance().getReference();



        menu_bottom = findViewById(R.id.navigation);

        setupfirebaseauth();






        menu_bottom.setItemSelected(0, true);

        if (savedInstanceState == null){
            menu_bottom.setItemSelected(R.id.explore, true);
            fragmentManager = getSupportFragmentManager();
            DiscoverFragment discoverFragment = new DiscoverFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, discoverFragment)
                    .commit();
        }


        menu_bottom.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment = null;
                switch(id) {
                    case R.id.explore:
                        fragment = new DiscoverFragment();
                        break;
                    case R.id.chats:
                        fragment = new ChatsFragment();
                        break;
                    case R.id.projects:
                        fragment = new MyProjectsFragment();
                        break;
                    case R.id.profile:
                        fragment = new profilefragment();
                        break;
                }

                if (fragment != null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .commit();
                }
                else {
                    Log.e(TAG, "Error in creating fragment");
                }
            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthstatelistner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthstatelistner!=null){
            mAuth.removeAuthStateListener(mAuthstatelistner);
        }
    }


    private void openProjectListings() {
        Intent intent = new Intent(this, ExploreProjectListings.class);
        startActivity(intent);

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

    private void setupfirebaseauth(){
        Log.d(TAG, "Setup FirebaseAuth");
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase mFirebasedatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mFirebasedatabase.getReference();;

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

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = firebaseMethod.getUserData(snapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void status(String status){
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users_display").child(fuser.getUid());
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("status", status);

        mref.updateChildren(hashmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
