package com.example.infosys1d_amigoproject.profilesetup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infosys1d_amigoproject.MainActivity;
import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileSetupLookingFor extends AppCompatActivity {
    private static final String TAG = "ProfileSetupAboutMe";
    TextInputLayout aboutme;
    Button nextbutton, prevbutton, skipbutton;

    private Userdataretrieval mUserSettings;

    //Firebase Database
    private FirebaseDatabase mFirebasedatabase;
    private DatabaseReference myRef;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;
    private FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup_looking_for);

        aboutme = findViewById(R.id.aboutme);
        nextbutton = findViewById(R.id.nextbuttonaboutme);
        prevbutton = findViewById(R.id.prevbuttonaboutme);
        skipbutton =  findViewById(R.id.skipsetupbuttonlookingfor);
        firebaseMethods = new FirebaseMethods(ProfileSetupLookingFor.this);
        setupfirebaseauth();

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileSettings();
            }
        });

        skipbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileSetupLookingFor.this, MainActivity.class));
            }
        });




    }

    private void saveProfileSettings() {
        final String lookingfortext = aboutme.getEditText().getText().toString();

        final String aboutmetext = getIntent().getStringExtra("About Me");
        Intent intent = new Intent(ProfileSetupLookingFor.this, ProfileSetupSkills.class);
        intent.putExtra("About Me", aboutmetext);
        intent.putExtra("Looking For", lookingfortext);
        startActivity(intent);

    }


    //------------------------------------------ Firebase ----------------------------------------------------------------------------------------------------

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
        myRef = mFirebasedatabase.getReference();;
        String userID = mAuth.getCurrentUser().getUid();

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
                //retrieve user information from database
                mUserSettings = firebaseMethods.getUserData(snapshot);
                //retrieve profile pic from database

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}