package com.example.infosys1d_amigoproject.profilesetup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infosys1d_amigoproject.MainActivity;
import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.profilemanagement_tab.profileactivity;
import com.example.infosys1d_amigoproject.projectmanagement_tab.CreateNewProject;
import com.example.infosys1d_amigoproject.projectmanagement_tab.Project;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class ProfileSetupProfilePic extends AppCompatActivity {
    ImageView profilepic;
    private static final String TAG = "ProfileSetupAboutMe";
    TextInputLayout aboutme;
    Button nextbutton, prevbutton;
    Button upload_from_gallery;
    Uri imageUri, downloadUrl;
    String randomKey;
    StorageReference storageRef;
    Userdataretrieval mUserSettings;
    private FirebaseDatabase mFirebasedatabase;
    private DatabaseReference firebaseReference;
    private DatabaseReference myRef;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;
    private FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup_profile_pic);
        aboutme = findViewById(R.id.aboutme);
        nextbutton = findViewById(R.id.nextbuttonaboutme);
        prevbutton = findViewById(R.id.prevbuttonaboutme);

        firebaseMethods = new FirebaseMethods(ProfileSetupProfilePic.this);
        storageRef = FirebaseStorage.getInstance().getReference();
        firebaseReference = FirebaseDatabase.getInstance().getReference();
        setupfirebaseauth();
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

        nextbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveProfileSettings();
                saveProfileSettings();

                updatesetup(true);
                Intent intent = new Intent(ProfileSetupProfilePic.this, MainActivity.class);
                (ProfileSetupProfilePic.this).finish();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

    }
    private void saveProfileSettings(){
        final String aboutme = getIntent().getStringExtra("About Me");
        final String lookingFor = getIntent().getStringExtra("Looking For");
        final String skillstext = getIntent().getStringExtra("Skills");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {








                if (!mUserSettings.getUsersdisplay().getAbout_me().equals(aboutme)){
                    firebaseMethods.updateAboutMe(aboutme);
                    if (!mUserSettings.getUsersdisplay().getLooking_for().equals(lookingFor)){
                        firebaseMethods.updateLookingFor(lookingFor);
                        if (!mUserSettings.getUsersdisplay().getSkills().toString().toString().equals(skillstext)){
                            firebaseMethods.updateSkillChips(skillstext);
                            Toast.makeText(ProfileSetupProfilePic.this, "You have completed your setup!", Toast.LENGTH_SHORT).show();
                            if(mUserSettings.getUsersdisplay().isCompeletedsetup()) {
                                System.out.println("ITS TRUE 92383312");


                            }

                        }
                }


                }



//                if (!mUserSettings.getUsersprivate().getEmail().equals(Email)){
//                    if(firebaseMethods.checkifemailexists(Email, dataSnapshot)== true){
//                    firebaseMethods.updateEmail(Email);} else{Toast.makeText(getActivity(), "Email already exists!", Toast.LENGTH_SHORT).show();}
//                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    private void updatesetup(boolean setup){
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users_display").child(fuser.getUid());
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("compeletedsetup", setup);

        mref.updateChildren(hashmap);
    }
    private void uploadpicture() {
        StorageReference newRef = storageRef.child("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
        newRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri;
                                System.out.println("123456789" + downloadUrl.toString());
                                FirebaseDatabase.getInstance().getReference().child("users_display").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_picture").setValue(downloadUrl.toString());


                            }
                        });

                    };
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }


    @Override
    public void onStart() {
        super.onStart();


        mAuth.addAuthStateListener(mAuthstatelistner);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthstatelistner != null) {
            mAuth.removeAuthStateListener(mAuthstatelistner);
        }
    }

    //FirebaseAuth
    private void setupfirebaseauth() {
        Log.d(TAG, "Setup FirebaseAuth");

        mAuth = FirebaseAuth.getInstance();
        mFirebasedatabase = FirebaseDatabase.getInstance();
        myRef = mFirebasedatabase.getReference();
        ;
        String userID = mAuth.getCurrentUser().getUid();

        //check if user is sign in
        mAuthstatelistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in" + user.getUid());
                } else {
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //retrieve user information from database
                mUserSettings = firebaseMethods.getUserData(snapshot);
                //retrieve profile pic from database

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}