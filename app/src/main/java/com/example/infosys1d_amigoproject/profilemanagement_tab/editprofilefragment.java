package com.example.infosys1d_amigoproject.profilemanagement_tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
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

//Edit profile fragment used whenuser accesses their own profile and thereafter clicks the pen icon to edit.
public class editprofilefragment extends Fragment {
    private static final String TAG = "editprofilefragment";

    private TextView mName, mBio, mAboutme, mlookingfor, mcurrentproject, muserid, mEmail, mSkills;
    private ImageView mProfilepic;
    private ImageView backToProfile;
    private TextView mChangeProfilePic;
    private Context mcontext;
    private String userID;
    private Userdataretrieval mUserSettings;


    //Firebase Database
    private FirebaseDatabase mFirebasedatabase;
    private DatabaseReference myRef;
    private ChipGroup mskills;
    private String selectedChipData;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;
    private FirebaseMethods firebaseMethods;


    FirebaseStorage storage;
    StorageReference storageref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater inflater2 = LayoutInflater.from(container.getContext());
        View view =  inflater2.inflate(R.layout.fragment_editprofilefragment, container, false);
        storage = FirebaseStorage.getInstance();
        storageref = storage.getReference();
        mName = view.findViewById(R.id.name);
        backToProfile = view.findViewById(R.id.backArrow);
        mBio = view.findViewById(R.id.bio);
        mAboutme = view.findViewById(R.id.aboutme);
        mlookingfor = view.findViewById(R.id.lookingfor);


        mEmail = view.findViewById(R.id.email);
        mcontext = getActivity();
        firebaseMethods = new FirebaseMethods(mcontext);
        setupfirebaseauth();
        ImageView checkMark = view.findViewById(R.id.saveChanges);

        backToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick, navigating to : profile fragment");
                profilefragment fragment = new profilefragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack("profilefragment");
                transaction.commit();
            }
        });
        checkMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: attempting to save changes.");
                saveProfileSettings();
            }
        });

        String[] skillsList = mcontext.getResources().getStringArray(R.array.skills_list);

        mskills = view.findViewById(R.id.collectionofskillchips);

        LayoutInflater inflater_0 = LayoutInflater.from(mcontext);
        for(String text: skillsList){
            Chip newChip = (Chip) inflater_0.inflate(R.layout.chip_filter,null,false);
            newChip.setText(text);
            mskills.addView(newChip);}
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
        for(int i = 0; i<mskills.getChildCount(); i++){
            Chip chip = (Chip) mskills.getChildAt(i);
            if ( displaydata.getSkills().contains(chip.getText().toString())){
                chip.setChecked(true);
            }
        }
        mEmail.setText(privatedata.getEmail());
    }

    private void saveProfileSettings(){
        final String displayName = mName.getText().toString();
        final String Bio = mBio.getText().toString();
        final String aboutMe = mAboutme.getText().toString();
        final String lookingFor = mlookingfor.getText().toString();

        selectedChipData = "";
        for(int i = 0; i<mskills.getChildCount(); i++){
            Chip chip = (Chip)mskills.getChildAt(i);
            if(chip.isChecked()){
                selectedChipData += (chip.getText().toString() + " ");
            }
        }
        final String skillChipsString = selectedChipData.trim();
        final String Email = mEmail.getText().toString();

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

                if (!mUserSettings.getUsersdisplay().getSkills().toString().equals(skillChipsString)){
                    firebaseMethods.updateSkillChips(skillChipsString);
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