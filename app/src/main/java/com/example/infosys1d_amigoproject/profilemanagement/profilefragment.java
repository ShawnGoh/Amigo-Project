package com.example.infosys1d_amigoproject.profilemanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.infosys1d_amigoproject.MainActivity;
import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.models.users_display;
import com.example.infosys1d_amigoproject.models.users_private;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class profilefragment extends Fragment {

    private static final String TAG = "profilefragment";
    private TextView mName, mBio, mAboutme, mlookingfor, mcurrentproject, muserid, memail;
    private ImageView mProfilepic;
    private Button changeProfilePic;
    private Context mcontext;
    private Button backtohomebutton;
    private ChipGroup mskills;

    //Firebase Database
    private FirebaseDatabase mFirebasedatabase;
    private DatabaseReference myRef;
    StorageReference storageReference;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;
    private FirebaseMethods firebaseMethods;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container,false);
        Log.d(TAG, "onCreateView: init widgets");
        mName = view.findViewById(R.id.profilenametextview);
        mBio = view.findViewById(R.id.profilebiotextview);
        mAboutme = view.findViewById(R.id.profileaboutmetextview);
        mlookingfor = view.findViewById(R.id.profilelookingfortextview);
        mcurrentproject = view.findViewById(R.id.profilecurrentprojectstextview);
        mProfilepic = view.findViewById(R.id.profilepic);
        mcontext = getActivity();
        muserid = view.findViewById(R.id.profileuserid);
        memail = view.findViewById(R.id.profileemailtextview);
        backtohomebutton = view.findViewById(R.id.backtohomepagebutton);
        changeProfilePic = view.findViewById(R.id.button2);
        mskills = view.findViewById(R.id.profileskillchipsgroup);
        Log.d(TAG, "onCreateView: widgets inited");
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseMethods = new FirebaseMethods(mcontext);
        StorageReference profileRef = storageReference.child("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(mProfilepic);
            }
        });
        setupfirebaseauth();
        Button editProfile = (Button) view.findViewById(R.id.editprofilebutton);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick, navigating to : edit profile fragment");
                editprofilefragment fragment = new editprofilefragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack("editprofilefragment");
                transaction.commit();
            }
        });
        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);

            }
        });


        backtohomebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mcontext, MainActivity.class));
            }
        });



        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000){
            if (resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
                System.out.println("uri"+imageUri.toString());
                mProfilepic.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri){
        StorageReference fileRef = storageReference.child("users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(mProfilepic);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setProfileWidgets(Userdataretrieval userSettings){
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieved from firebase database");

        users_display displaydata = userSettings.getUsersdisplay();
        users_private privatedata = userSettings.getUsersprivate();

        mName.setText(displaydata.getName());
        mBio.setText(displaydata.getBio());
        mAboutme.setText(displaydata.getAbout_me());
        mlookingfor.setText(displaydata.getLooking_for());
        mcurrentproject.setText(displaydata.getCurrent_projects().toString());
        muserid.setText(privatedata.getUser_id());
        memail.setText(privatedata.getEmail());



        ArrayList<String> chipslist = displaydata.getSkills();
        LayoutInflater inflater = LayoutInflater.from(mcontext);

        for(String text: chipslist){
            Chip newchip = (Chip) inflater.inflate(R.layout.chip_item,null,false);
            newchip.setText(text);
            mskills.addView(newchip);}


    }

    //------------------------------------------ Firebase ----------------------------------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthstatelistner);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthstatelistner!=null){
            mAuth.removeAuthStateListener(mAuthstatelistner);
        }
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
                }
                else{
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //retrieve user information from database
                setProfileWidgets(firebaseMethods.getUserData(snapshot));
                //retrieve profile pic from database

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}