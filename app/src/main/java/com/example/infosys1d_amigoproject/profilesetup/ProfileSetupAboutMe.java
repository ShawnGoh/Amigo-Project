package com.example.infosys1d_amigoproject.profilesetup;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileSetupAboutMe extends AppCompatActivity {

    private static final String TAG = "ProfileSetupAboutMe";
    TextInputLayout aboutme;
    Button nextbutton, prevbutton;

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
        setContentView(R.layout.activity_profile_setup_about_me);

        aboutme = findViewById(R.id.aboutme);
        nextbutton = findViewById(R.id.nextbuttonaboutme);
        prevbutton = findViewById(R.id.prevbuttonaboutme);



    }

    private void saveProfileSettings() {
        final String aboutmetext = aboutme.getEditText().getText().toString();

    }


    //------------------------------------------ Firebase ----------------------------------------------------------------------------------------------------

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


    //FirebaseAuth
    private void setupfirebaseauth(){
        Log.d(TAG, "Setup FirebaseAuth");

        mAuth = FirebaseAuth.getInstance();
        mFirebasedatabase = FirebaseDatabase.getInstance();
        myRef = mFirebasedatabase.getReference();;




        //check if user is sign in
        mAuthstatelistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user  = firebaseAuth.getCurrentUser();


                if(user !=null){
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in" +user.getUid());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //add new users_display to database
//                            firebaseMethods.addNewUser(firstnamesignup, lastnamesignup, email);
                            Toast.makeText(getApplicationContext(), "Account created, sending verification email", Toast.LENGTH_LONG).show();
                            //Sign user out as need verification email
                            mAuth.signOut();


                            //add new users_private to database

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                else{
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };

    }




}