package com.example.infosys1d_amigoproject.profilemanagement_tab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.MyAdapter;
import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.models.users_display;
import com.example.infosys1d_amigoproject.models.users_private;
import com.example.infosys1d_amigoproject.projectmanagement_tab.Project;
import com.example.infosys1d_amigoproject.signinsignup.SignIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
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
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


public class OtherProfile2 extends AppCompatActivity {
    MyAdapter myAdapter;
    private static final String TAG = "profilefragment";
    private TextView mName, mBio, mAboutme, mlookingfor,  muserid, memail;
    private ImageView mProfilepic;
    private Button changeProfilePic, signoutbutton;
    private Context mcontext = OtherProfile2.this;
    private ImageButton editProfile;
    private ChipGroup mskills;

    //Firebase Database
    private FirebaseDatabase mFirebasedatabase;
    private DatabaseReference databaseReference;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    //Firebase Auth
    private FirebaseAuth mAuth ;
    private FirebaseAuth.AuthStateListener mAuthstatelistner;
    private FirebaseMethods firebaseMethods;

    RecyclerView recyclerView;


    private Uri imageUri, downloadUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.fragment_other_profile);
        Log.d(TAG, "onCreateView: init widgets");

        mName = findViewById(R.id.profilenametextview);
//      mBio = view.findViewById(R.id.profilebiotextview);
        mAboutme = findViewById(R.id.profileaboutmetextview);
        mlookingfor = findViewById(R.id.profilelookingfortextview);
        mProfilepic = findViewById(R.id.profilepic);
        recyclerView = findViewById(R.id.suggestedRecycler2);
        recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));

//      muserid = view.findViewById(R.id.profileuserid);
        memail = findViewById(R.id.profileemailtextview);
//      backtohomebutton = view.findViewById(R.id.backtohomepagebutton);
        changeProfilePic = findViewById(R.id.Changepicturebutton);
        mskills = findViewById(R.id.profileskillchipsgroup);
        editProfile = findViewById(R.id.editprofilebutton);
        signoutbutton =findViewById(R.id.signoutbutton);

        Log.d(TAG, "onCreateView: widgets inited");


        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseMethods = new FirebaseMethods(mcontext);
        StorageReference newRef = storageReference.child("images/" + firebaseMethods.getUserID());
        newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(mProfilepic);
            }
        });

//        setupfirebaseauth();


        //OnClickListeners

        signoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                startActivity(new Intent(mcontext, SignIn.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick, navigating to : edit profile fragment");
                editprofilefragment fragment = new editprofilefragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack("editprofilefragment");
                transaction.commit();
            }
        });

        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery,1);

            }
        });


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
                        if (firebaseMethods.getUserData(snapshot).getUsersprivate().getUser_id() != userID){
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

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData() != null){
            imageUri = data.getData();
            mProfilepic.setImageURI(imageUri);
            uploadpicture();
        }
    }
    private void uploadpicture() {
        StorageReference newRef = storageReference.child("images/" + firebaseMethods.getUserID());
        newRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        newRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri;
                                firebaseMethods.updateProfilePicture(downloadUrl.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

}
