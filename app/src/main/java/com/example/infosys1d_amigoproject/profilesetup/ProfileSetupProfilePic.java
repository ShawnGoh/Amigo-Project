package com.example.infosys1d_amigoproject.profilesetup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infosys1d_amigoproject.MainActivity;
import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.projectmanagement_tab.CreateNewProject;
import com.example.infosys1d_amigoproject.projectmanagement_tab.Project;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class ProfileSetupProfilePic extends AppCompatActivity {
    ImageView profilepic;
    Button upload_from_gallery;
    Uri imageUri, downloadUrl;
    String randomKey;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup_profile_pic);

        storageRef = FirebaseStorage.getInstance().getReference();


        profilepic = findViewById(R.id.circleImageView);

        upload_from_gallery = findViewById(R.id.profilesetupprofilepic);
        upload_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery,1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData() != null){
            imageUri = data.getData();
            profilepic.setImageURI(imageUri);
            uploadpicture();
        }
        else{
            profilepic.setImageResource(R.mipmap.ic_launcher_round);
        }
    }

    private void uploadpicture() {
        randomKey = UUID.randomUUID().toString();
        StorageReference newRef = storageRef.child("images/" + randomKey);
        newRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri;
                            }
                        });
                        Snackbar.make(findViewById(R.id.upload), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                    };
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        System.out.println(getIntent().getStringExtra("About Me")+ "92383312");
        System.out.println(getIntent().getStringExtra("Looking For")+ "92383312");
        System.out.println(getIntent().getStringExtra("Skills")+ "92383312");
    }
}