package com.example.infosys1d_amigoproject.profilemanagement;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.infosys1d_amigoproject.MainActivity;
import com.example.infosys1d_amigoproject.R;

public class profileactivity extends AppCompatActivity {

    private static final String TAG = "profileactivity";

    private Context mcontext = profileactivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profileactivity);

//
//



        init();
    }


    private void init(){
        Log.d(TAG, "init: inflating profile fragment");

        profilefragment fragment = new profilefragment();
        FragmentTransaction transaction = profileactivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.profilecontainer, fragment);
        transaction.addToBackStack("profilefragment");
        transaction.commit();
    }
}