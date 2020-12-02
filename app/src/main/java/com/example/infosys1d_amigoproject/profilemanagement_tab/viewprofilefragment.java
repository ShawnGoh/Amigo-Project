package com.example.infosys1d_amigoproject.profilemanagement_tab;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.MainActivity;
import com.example.infosys1d_amigoproject.MyAdapter;
import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.chat_tab.MessageActivity;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.models.users_display;
import com.example.infosys1d_amigoproject.models.users_private;
import com.example.infosys1d_amigoproject.projectmanagement_tab.Project;
import com.example.infosys1d_amigoproject.signinsignup.SignIn;
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

public class viewprofilefragment extends Fragment {
    MyAdapter myAdapter;
    private static final String TAG = "profilefragment";
    private TextView mName, mBio, mAboutme, mlookingfor,  muserid, memail;
    private ImageView mProfilepic;
    private Button backButton;
    private Context mcontext;
    private ImageButton editProfile;
    private ChipGroup mskills;
    private Userdataretrieval mUser;

    //Firebase Database
    private FirebaseDatabase mFirebasedatabase;
    private DatabaseReference databaseReference;
    private users_display user;
    private users_private user2;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;
    private FirebaseMethods firebaseMethods;

    RecyclerView recyclerView;


    private Uri imageUri, downloadUrl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewprofile, container,false);


        Log.d(TAG, "onCreateView: init widgets");

        mName = view.findViewById(R.id.profilenametextview);
//      mBio = view.findViewById(R.id.profilebiotextview);
        mAboutme = view.findViewById(R.id.profileaboutmetextview);
        mlookingfor = view.findViewById(R.id.profilelookingfortextview);
        mProfilepic = view.findViewById(R.id.profilepic);
        recyclerView = view.findViewById(R.id.suggestedRecycler2);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mcontext = getActivity();
//      muserid = view.findViewById(R.id.profileuserid);
        memail = view.findViewById(R.id.profileemailtextview);
//      backtohomebutton = view.findViewById(R.id.backtohomepagebutton);
        backButton = view.findViewById(R.id.backbutton);
        mskills = view.findViewById(R.id.profileskillchipsgroup);
//        editProfile = view.findViewById(R.id.editprofilebutton);
//        signoutbutton = view.findViewById(R.id.signoutbutton);
        try {
            mUser = getUserfromBundle();
        } catch(NullPointerException e){
            Log.d(TAG, "Null Pointer Exception 92383312");
            getActivity().getSupportFragmentManager().popBackStack();

        }
        Log.d(TAG, "onCreateView: widgets inited");


//        storageReference = FirebaseStorage.getInstance().getReference();
//        firebaseMethods = new FirebaseMethods(mcontext);
//        StorageReference newRef = storageReference.child("images/" + mUser.getUsersprivate().getUser_id());
//        newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                Picasso.get().load(uri).into(mProfilepic);
//            }
//        });

        setupfirebaseauth();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(mcontext, MainActivity.class));
            }
        });

        //OnClickListeners

//        signoutbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mAuth.signOut();
//                startActivity(new Intent(mcontext, SignIn.class));
//                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//            }
//        });


//        editProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick, navigating to : edit profile fragment");
//                editprofilefragment fragment = new editprofilefragment();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, fragment);
//                transaction.addToBackStack("editprofilefragment");
//                transaction.commit();
//            }
//        });
//
//        changeProfilePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent gallery = new Intent();
//                gallery.setType("image/*");
//                gallery.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(gallery,1);
//
//            }
//        });


        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference projref = databaseReference.child("Projects");
        ArrayList<Project> projectList = new ArrayList<>();
        myAdapter = new MyAdapter(projectList);
        recyclerView.setAdapter(myAdapter);


        projref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                projectList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Project project = postSnapshot.getValue(Project.class);
                    for (String userID: project.getUsersinProject()){
                        if (mUser.getUsersprivate().getUser_id() != userID){
                            projectList.add(project);
                        }
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: ");
            }
        });
        return view;
    }

    private Userdataretrieval getUserfromBundle(){
        Bundle bundle = this.getArguments();

        if(bundle!=null){
            final String getID = bundle.getString("Intent User");
            DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users_display").child(getID);
            System.out.println(mref +"92383312");
            DatabaseReference mref2 = FirebaseDatabase.getInstance().getReference("users_private").child(getID);
            System.out.println(mref2 + "92383312");
            mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            user= snapshot.getValue(users_display.class);


                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            mref2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    user2= snapshot.getValue(users_private.class);
                    mUser = new Userdataretrieval(user, user2);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            return mUser;
        } else{
            return null;
        }

    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData() != null){
//            imageUri = data.getData();
//            mProfilepic.setImageURI(imageUri);
//            uploadpicture();
//        }
//    }
//    private void uploadpicture() {
//        StorageReference newRef = storageReference.child("images/" + firebaseMethods.getUserID());
//        newRef.putFile(imageUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                                downloadUrl = uri;
//                                firebaseMethods.updateProfilePicture(downloadUrl.toString());
//                            }
//                        });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                });
//    }
    private void setProfileWidgets(Userdataretrieval userSettings){
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieved from firebase database");

        users_display displaydata = userSettings.getUsersdisplay();
        users_private privatedata = userSettings.getUsersprivate();

        mName.setText(displaydata.getName());
//        mBio.setText(displaydata.getBio());
        mAboutme.setText(displaydata.getAbout_me());
        mlookingfor.setText(displaydata.getLooking_for());

//        muserid.setText(privatedata.getUser_id());
        System.out.println(privatedata.getEmail() + "92383312");
        memail.setText(privatedata.getEmail());
        Picasso.get().load(displaydata.getProfile_picture()).into(mProfilepic);




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
        databaseReference = mFirebasedatabase.getReference();;

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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //retrieve user information from database
//                setProfileWidgets(firebaseMethods.getUserData(snapshot));
                setProfileWidgets(mUser);
                //retrieve profile pic from database

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}