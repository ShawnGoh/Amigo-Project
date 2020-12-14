package com.example.infosys1d_amigoproject.signinsignup;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.regex.Pattern;

//User sign up activity
public class SignUp extends AppCompatActivity {

    private static final String TAG = "SignUp";

    private Button signupbutton;
    private TextView haveaccount, signupinstru, welcomesignup;
    private TextInputLayout emailsignup, passwordsignup, firstname, lastname;
    private CheckBox termscheckbox;
    private LinearLayout signuptextinputs, signupscreen;
    private ImageView Logo;
    private ProgressBar signuploadingbar;

    //store some strings for use through class
    private String firstnamesignup,lastnamesignup,email,password;
    boolean emailexists;

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
        setContentView(R.layout.activity_sign_up);


        //initialize UI elements
        signupbutton = findViewById(R.id.signupconfirmbutton);
        haveaccount = findViewById(R.id.alreadyhaveaccount);
        emailsignup = findViewById(R.id.Emailsignup);
        passwordsignup = findViewById(R.id.Passwordsignup);
        termscheckbox = findViewById(R.id.termscheckbox);
        signuptextinputs = findViewById(R.id.signuptextinputs);
        firstname = findViewById(R.id.FirstName);
        lastname = findViewById(R.id.LastName);
        Logo = findViewById(R.id.logoimagesignup);
        welcomesignup = findViewById(R.id.welcomesignup);
        signupinstru = findViewById(R.id.instrusignup);
        signupscreen = findViewById(R.id.signupscreen);
        signuploadingbar = findViewById(R.id.signuploadingbar);


        signuploadingbar.setVisibility(View.GONE);

        setupfirebaseauth();
        firebaseMethods = new FirebaseMethods(SignUp.this);


        init();

        //dont have account go to signin page
        //pairs used for animation between SignIn/SignUp pages
        haveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pair[] pairs = new Pair[8];
                pairs[0] = new Pair<View, String>(Logo, "LogoSignInUp");
                pairs[1] = new Pair<View, String>(welcomesignup, "WelcomeSignInUp");
                pairs[2] = new Pair<View, String>(signupinstru, "InstruSignInUp");
                pairs[3] = new Pair<View, String>(emailsignup, "emailtrans");
                pairs[4] = new Pair<View, String>(passwordsignup, "passwordtrans");
                pairs[5] = new Pair<View, String>(signupbutton, "signupsigninbutton");
                pairs[6] = new Pair<View, String>(haveaccount, "LoginLogouttoggle");
                pairs[7] = new Pair<View, String>(signupscreen, "screentransit");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
                startActivity((new Intent(getApplicationContext(), SignIn.class)),options.toBundle());
            }
        });

    }

    private Boolean validatefirstname(){
        String val = firstname.getEditText().getText().toString();
        String checknumbers =".*\\d.*";
        if(val.isEmpty()){
            firstname.setError("Please enter your first name");
            firstname.setFocusable(true);
            return false;
        }
        else if(val.matches(checknumbers)){
            firstname.setError("Please enter a valid first name");
            firstname.setFocusable(true);
            return false;
        }
        else {
            firstname.setError(null);
            firstname.setFocusable(false);
            return true;
        }
    }

    private Boolean validatelastname(){
        String val = lastname.getEditText().getText().toString().trim();
        String checknumbers =".*\\d.*";
        if(val.isEmpty()){
            lastname.setError("Please enter your last name");
            lastname.setFocusable(true);
            return false;
        }
        else if(val.matches(checknumbers)){
            lastname.setError("Please enter a valid first name");
            lastname.setFocusable(true);
            return false;
        }
        else {
            lastname.setError(null);
            lastname.setFocusable(false);
            return true;
        }
    }

    private Boolean validateemail(){
        final String emailval = emailsignup.getEditText().getText().toString().trim();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users_private");



        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "validateemail: checking database");
                if(firebaseMethods.checkifemailexists(emailval, snapshot)){
                    emailsignup.setError("Email already in use");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Cant access database");
                throw error.toException();
            }
        });



        if(emailval.isEmpty()){
            emailsignup.setError("Please enter a valid email");
            emailsignup.setFocusable(true);
            return false;
        }
        else if(!isValidEmailId(emailval)){
            emailsignup.setError("Invalid email address");
            emailsignup.setFocusable(true);
            return false;
        }
        else {
            emailsignup.setError(null);
            emailsignup.setFocusable(false);
            return true;
        }
    }

    private boolean isValidEmailId(String email){
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    private Boolean validatepassword(){
        String val = passwordsignup.getEditText().getText().toString();
        String passwordvalidation = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,}$";
        //^                 # start-of-string
        //(?=.*[0-9])       # a digit must occur at least once
        //(?=.*[a-z])       # a lower case letter must occur at least once
        //(?=.*[A-Z])       # an upper case letter must occur at least once
        //(?=.*[@#$%^&+=])  # a special character must occur at least once you can replace with your special characters
        //(?=\\S+$)          # no whitespace allowed in the entire string
        //.{4,}             # anything, at least six places though
        //$                 # end-of-string

        if(val.isEmpty()){
            passwordsignup.setError("Please enter a password");
            return false;
        }
        else if(!val.matches(passwordvalidation)){
            passwordsignup.setError("Min 6 characters 1 uppercase, 1 lowercase, 1 number");
            return false;
        }
        else {
            passwordsignup.setError(null);
            return true;
        }
    }

    private boolean validatetermscheck(){
        if(termscheckbox.isChecked()){
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(), "Please agree to the terms before use", Toast.LENGTH_SHORT).show();
            termscheckbox.setFocusable(true);
            return false;}
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


    private void init(){
        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebasedatabase = FirebaseDatabase.getInstance();
                myRef = mFirebasedatabase.getReference("users_private");

                if(validatefirstname() && validatelastname() && validateemail() && validatepassword()&&validatetermscheck()){
                    signuploadingbar.setVisibility(View.VISIBLE);

                    firstnamesignup = firstname.getEditText().getText().toString();
                    lastnamesignup = lastname.getEditText().getText().toString();
                    email = emailsignup.getEditText().getText().toString();
                    password = passwordsignup.getEditText().getText().toString();

                    firebaseMethods.registerNewEmail(firstnamesignup,lastnamesignup,email,password);

                    signuploadingbar.setVisibility(View.GONE);
                }

            }
        });
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
                            firebaseMethods.addNewUser(firstnamesignup, lastnamesignup, email);
                            Toast.makeText(getApplicationContext(), "Account created, sending verification email", Toast.LENGTH_LONG).show();
                            //Sign user out as need verification email
                            mAuth.signOut();


                            //add new users_private to database

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    Pair[] pairs = new Pair[8];
                    pairs[0] = new Pair<View, String>(Logo, "LogoSignInUp");
                    pairs[1] = new Pair<View, String>(welcomesignup, "WelcomeSignInUp");
                    pairs[2] = new Pair<View, String>(signupinstru, "InstruSignInUp");
                    pairs[3] = new Pair<View, String>(emailsignup, "emailtrans");
                    pairs[4] = new Pair<View, String>(passwordsignup, "passwordtrans");
                    pairs[5] = new Pair<View, String>(signupbutton, "signupsigninbutton");
                    pairs[6] = new Pair<View, String>(haveaccount, "LoginLogouttoggle");
                    pairs[7] = new Pair<View, String>(signupscreen, "screentransit");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this, pairs);
                    startActivity((new Intent(getApplicationContext(), SignIn.class)),options.toBundle());
                    finish();

                }
                else{
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };

    }





}