package com.example.infosys1d_amigoproject.profilemanagement_tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.infosys1d_amigoproject.R;

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
            Toast.makeText(mcontext, "VIEWING PROFILE", Toast.LENGTH_SHORT).show();
            }
//        else if{
//                Toast.makeText(mcontext, "something went wrong", Toast.LENGTH_SHORT).show();
//            }

        else{
            profilefragment fragment = new profilefragment();
            FragmentTransaction transaction = profileactivity.this.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.profilecontainer, fragment);
            transaction.addToBackStack("profilefragment");
            transaction.commit();
            Toast.makeText(mcontext, "WRONGGGG", Toast.LENGTH_SHORT).show();
        }

    }
}