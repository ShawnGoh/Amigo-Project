package com.example.infosys1d_amigoproject.projectmanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import com.example.infosys1d_amigoproject.models.users_display;

public class CreateNewProject extends AppCompatActivity {

    ImageView imageView;
    TextInputLayout textInputLayout;
    Button button, create_project;
    private static final int PICK_IMAGE = 1;
    public Uri imageUri;
    public Uri downloadUrl;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myref2;
    FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_project);
        imageView = findViewById(R.id.imageView2);
        textInputLayout = findViewById(R.id.textInputLayout);

        button = findViewById(R.id.button);
        create_project = findViewById(R.id.createproject);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myref2 = firebaseDatabase.getReference();
        firebaseMethods = new FirebaseMethods(this.getApplicationContext());
        System.out.println(firebaseMethods.getUserID());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(gallery,1);
            }
        });
        create_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Project project = new Project("test", textInputLayout.getEditText().getText().toString(),
                        "description",new ArrayList<String>(Arrays.asList("Java", "Jsim")),
                        new ArrayList<String>(Arrays.asList("Yv9FUajYYRM3gFp4rDxClm4XdXM2",firebaseMethods.getUserID())),
                        firebaseMethods.getUserID());
                String postId = myref2.child("Projects").push().getKey();
                myref2.child(postId).setValue(project);
                myref2.child("Projects").child("test").setValue("123");
                firebaseMethods.updateName("hello");
                System.out.println(firebaseMethods.getUserID());
                System.out.println("test");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData() != null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
            uploadpicture();
        }
    }

    private void uploadpicture() {
        final String randomKey = UUID.randomUUID().toString();
        StorageReference newRef = storageRef.child("images/" + randomKey);
        newRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Uri downloadUrl = uri;
                                System.out.println(downloadUrl.toString());

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

    }
}