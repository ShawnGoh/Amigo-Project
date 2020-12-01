package com.example.infosys1d_amigoproject.profilemanagement;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.models.users_display;
import com.example.infosys1d_amigoproject.models.users_private;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link editprofilefragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class editprofilefragment extends Fragment {
    private TextView mName, mBio, mAboutme, mlookingfor, mcurrentproject, muserid, mEmail, mSkills;
    private ImageView mProfilepic;
    private TextView mChangeProfilePic;
    private Context mcontext;
    private String userID;
    private Userdataretrieval mUserSettings;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "edit profile fragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Firebase Database
    private FirebaseDatabase mFirebasedatabase;
    private DatabaseReference myRef;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;
    private FirebaseMethods firebaseMethods;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseStorage storage;
    StorageReference storageref;


    public editprofilefragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment editprofilefragment.
     */
    // TODO: Rename and change types and number of parameters
    public static editprofilefragment newInstance(String param1, String param2) {
        editprofilefragment fragment = new editprofilefragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater inflater2 = LayoutInflater.from(container.getContext());
        View view =  inflater2.inflate(R.layout.fragment_editprofilefragment, container, false);
        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference();
        mName = view.findViewById(R.id.name);
        mBio = view.findViewById(R.id.bio);
        mAboutme = view.findViewById(R.id.aboutme);
        mlookingfor = view.findViewById(R.id.lookingfor);
        mSkills = view.findViewById(R.id.collectionofskillchips);
        mProfilepic = view.findViewById(R.id.profile_photo);
        mChangeProfilePic = view.findViewById(R.id.changeProfilePhoto);
        mEmail = view.findViewById(R.id.email);
        mcontext = getActivity();
        firebaseMethods = new FirebaseMethods(mcontext);
        setupfirebaseauth();
        ImageView checkMark = view.findViewById(R.id.saveChanges);
        checkMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes.");
                saveProfileSettings();
            }
        });


        return view;
    }



    private void setProfileWidgets(Userdataretrieval userSettings){
        Log.d(TAG, "setProfileWidgets: setting widgets with data retrieved from firebase database");
        mUserSettings = userSettings;
        users_display displaydata = userSettings.getUsersdisplay();
        users_private privatedata = userSettings.getUsersprivate();

        mName.setText(displaydata.getName());
        mBio.setText(displaydata.getBio());
        mAboutme.setText(displaydata.getAbout_me());
        mlookingfor.setText(displaydata.getLooking_for());
        mSkills.setText(displaydata.getSkills().toString().toString().replaceAll("\\[", "").replaceAll("\\]","").replaceAll("\\,", ""));
        mEmail.setText(privatedata.getEmail().toString());





    }
    private void saveProfileSettings(){
        final String displayName = mName.getText().toString();
        final String Bio = mBio.getText().toString();
        final String aboutMe = mAboutme.getText().toString();
        final String lookingFor = mlookingfor.getText().toString();
        final String skillChipsString = mSkills.getText().toString();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                if (!mUserSettings.getUsersdisplay().getName().equals(displayName)) {
                    checkIfNameExists(displayName);
                }
                if (!mUserSettings.getUsersdisplay().getBio().equals(Bio)){
                    firebaseMethods.updateBio(Bio);

                }
                if (!mUserSettings.getUsersdisplay().getAbout_me().equals(aboutMe)){
                    firebaseMethods.updateAboutMe(aboutMe);

                }
                if (!mUserSettings.getUsersdisplay().getLooking_for().equals(lookingFor)){
                    firebaseMethods.updateLookingFor(lookingFor);

                }

                if (!mUserSettings.getUsersdisplay().getSkills().toString().toString().equals(skillChipsString)){
                    firebaseMethods.updateSkillChips(skillChipsString);
//                    String str[] = skillChipsString.split(", ");
//                    List<String> al = new ArrayList<String>();
//                    al = Arrays.asList(str);
//                    mUserSettings.getUsersdisplay().setSkills((ArrayList<String>) al);

                }

                Toast.makeText(getActivity(), "Saved Changes", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkIfNameExists(String displayName) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.db_usersdisplay))
                .orderByChild(getString(R.string.field_name))
                .equalTo(displayName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    firebaseMethods.updateName(displayName);

                }
                for (DataSnapshot singleSnapshot: snapshot.getChildren()){
                    if(singleSnapshot.exists()){
                        Log.d(TAG, "checkIfNameExists: Found A Match: " + singleSnapshot.getValue(users_display.class).getName());
                        Toast.makeText(getActivity(), "That Name already Exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        userID = mAuth.getCurrentUser().getUid();

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