package com.example.infosys1d_amigoproject.projectmanagement_tab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.infosys1d_amigoproject.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProjectDetails extends AppCompatActivity {
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    StorageReference storageReference,projectref;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        mCollapsingToolbarLayout = findViewById(R.id.collapse);
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsingToolbarLayoutTitleColor);


        Intent intent = getIntent();
        String text = intent.getStringExtra("ProjectID");
        imageView = findViewById(R.id.project_picture);

        storageReference = FirebaseStorage.getInstance().getReference();
        projectref = storageReference.child("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        projectref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println(uri.toString());
                Picasso.get().load(uri).into(imageView);
            }
        });



    }
}