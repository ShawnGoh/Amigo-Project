package com.example.infosys1d_amigoproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.infosys1d_amigoproject.profilemanagement.profileactivity;
import com.example.infosys1d_amigoproject.signinsignup.SignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity/Homescreen";
    Button signoutbutton, profilepagebutton;



    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signoutbutton = findViewById(R.id.signoutbutton);
        profilepagebutton = findViewById(R.id.profilebutton);

        setupfirebaseauth();

        signoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        profilepagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), profileactivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

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