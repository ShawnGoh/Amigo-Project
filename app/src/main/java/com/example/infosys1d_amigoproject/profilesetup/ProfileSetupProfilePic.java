package com.example.infosys1d_amigoproject.profilesetup;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.infosys1d_amigoproject.R;

public class ProfileSetupProfilePic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup_profile_pic);
        System.out.println(getIntent().getStringExtra("About Me")+ "92383312");
        System.out.println(getIntent().getStringExtra("Looking For")+ "92383312");
        System.out.println(getIntent().getStringExtra("Skills")+ "92383312");
    }
}