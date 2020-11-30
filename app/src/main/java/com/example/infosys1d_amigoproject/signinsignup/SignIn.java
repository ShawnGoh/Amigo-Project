package com.example.infosys1d_amigoproject.signinsignup;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infosys1d_amigoproject.MainActivity;
import com.example.infosys1d_amigoproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private static final String TAG = "SignIn Activity";
    Button signinbutton;
    TextView donthaveaccount, welcomesignin, signininstru;
    TextInputLayout emailsignin, passwordsignin;
    ImageView Logoimage;
    LinearLayout signinscreen;
    ProgressBar loadingwheel;

    //firebase auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        //initialize UI elements
        signinbutton = findViewById(R.id.signinconfirmbutton);
        donthaveaccount = findViewById(R.id.donthaveaccount);
        emailsignin = findViewById(R.id.Emailsignin);
        passwordsignin = findViewById(R.id.Passwordsignin);
        Logoimage = findViewById(R.id.logoimagesignin);
        welcomesignin = findViewById(R.id.welcomesignin);
        signininstru = findViewById(R.id.instrusignin);
        signinscreen = findViewById(R.id.signinscreen);
        loadingwheel = findViewById(R.id.progressBar);

        Log.d(TAG, "onCreate Started");

        loadingwheel.setVisibility(View.GONE);

        setupfirebaseauth();

        init();

        //dont have account go to signup page
        donthaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair[] pairs = new Pair[8];
                pairs[0] = new Pair<View, String>(Logoimage, "LogoSignInUp");
                pairs[1] = new Pair<View, String>(welcomesignin, "WelcomeSignInUp");
                pairs[2] = new Pair<View, String>(signininstru, "InstruSignInUp");
                pairs[3] = new Pair<View, String>(emailsignin, "emailtrans");
                pairs[4] = new Pair<View, String>(passwordsignin, "passwordtrans");
                pairs[5] = new Pair<View, String>(signinbutton, "signupsigninbutton");
                pairs[6] = new Pair<View, String>(donthaveaccount, "LoginLogouttoggle");
                pairs[7] = new Pair<View, String>(signinscreen, "screentransit");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignIn.this, pairs);
                startActivity((new Intent(getApplicationContext(), SignUp.class)),options.toBundle());
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

    private boolean isStringNUll(String string){
        if(string.equals("")) return true;
        else return false;
    }

    private void init(){
        Button loginbutton = findViewById(R.id.signinconfirmbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "OnClick: Attempting to login");


                String email = emailsignin.getEditText().getText().toString();
                String password = passwordsignin.getEditText().getText().toString();

                if(!isStringNUll(email) && !isStringNUll(password)) {
                    loadingwheel.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                try {
                                    //Check if email has been verified
                                    if(user.isEmailVerified()){
                                        //If verified, sign in and proceed to home page
                                        Log.d(TAG, "onComplete: sucess. email is verified");
                                        Log.d(TAG, "signInWithEmail:success");
                                        loadingwheel.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Authentication Passed.", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        finish();
                                    }
                                    else{
                                        //If not verified, sign account out prevent login and ask user to verify
                                        Toast.makeText(getApplicationContext(), "Email has not been verified\nCheck your inbox.", Toast.LENGTH_SHORT).show();
                                        loadingwheel.setVisibility(View.GONE);
                                        mAuth.signOut();
                                    }
                                }catch (NullPointerException e){
                                    Log.e(TAG, "onComplete: NullPointerException "+e.getMessage());
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                loadingwheel.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                            loadingwheel.setVisibility(View.GONE);

                        }
                    });

                }
                else{
                    Toast.makeText(getApplicationContext(), "Fill in all fields", Toast.LENGTH_LONG).show();
                }

            }
        });
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