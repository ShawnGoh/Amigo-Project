package com.example.infosys1d_amigoproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.infosys1d_amigoproject.ChatsFragment;
import com.example.infosys1d_amigoproject.DiscoverFragment;
import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.profilemanagement.profilefragment;
import com.example.infosys1d_amigoproject.projectmanagement.ExploreProjectListings;
import com.example.infosys1d_amigoproject.projectmanagement.MyProjectsFragment;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // for testing purposes, Main Activity file should be compiled after every view has been completed.

    private static final String TAG = "MainActivity/Homescreen";
    Button signoutbutton, profilepagebutton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;

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
        myref.child("hello").setValue("yo whats up");

        signoutbutton = findViewById(R.id.signoutbutton);

        menu_bottom = findViewById(R.id.navigation);

        setupfirebaseauth();

        signoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


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
