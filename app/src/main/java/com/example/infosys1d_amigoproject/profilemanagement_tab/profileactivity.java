package com.example.infosys1d_amigoproject.profilemanagement_tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.infosys1d_amigoproject.R;

//Activity for when a user clicks on another user's picture to show their profile, holds the viewprofilefragment
//as viewing ow profile is different from viewing another person's profile.
public class profileactivity extends AppCompatActivity {

    private static final String TAG = "profileactivity";
    private Context mcontext = profileactivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileactivity);

        init();
    }


    private void init(){
        Log.d(TAG, "init: inflating profile fragment");

        Intent intent = getIntent();
        if(intent.hasExtra("Intent User")){

            Log.d(TAG, "init: inflating view profile");
            viewprofilefragment fragment = new viewprofilefragment();
            Bundle args = new Bundle();
            args.putString("Intent User",
                    intent.getStringExtra("Intent User"));
            fragment.setArguments(args);

            FragmentTransaction transaction = profileactivity.this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.profilecontainer, fragment);
            transaction.addToBackStack("viewprofilefragment");
            transaction.commit();
            }

        else{
            profilefragment fragment = new profilefragment();
            FragmentTransaction transaction = profileactivity.this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.profilecontainer, fragment);
            transaction.addToBackStack("profilefragment");
            transaction.commit();
        }

    }
}