package com.example.infosys1d_amigoproject.projectmanagement_tab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infosys1d_amigoproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProjectDetails extends AppCompatActivity {
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    StorageReference storageReference,projectref;
    DatabaseReference myref;
    ImageView imageView;
    Button clicktoChat;
    Button applytoJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        mCollapsingToolbarLayout = findViewById(R.id.collapse);

        imageView = findViewById(R.id.project_picture);
        Intent intent = getIntent();
        String project_id = intent.getStringExtra("ProjectID");
        myref = FirebaseDatabase.getInstance().getReference().child("Projects").child(project_id);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Project project = snapshot.getValue(Project.class);
                System.out.println(project.getThumbnail()+"   123456");
                Picasso.get().load(project.getThumbnail()).into(imageView);
//        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor);
//        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor);

        clicktoChat = findViewById(R.id.clicktoChat);
        clicktoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent to chat with project creator
            }
        });
        applytoJoin = findViewById(R.id.applytoJoin);
        applytoJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent to project application fragment OR fillable dialogue
            }
        });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("errror");
            }
        });

    }
}