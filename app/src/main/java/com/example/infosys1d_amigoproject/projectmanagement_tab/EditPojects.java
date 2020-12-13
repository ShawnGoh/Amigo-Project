package com.example.infosys1d_amigoproject.projectmanagement_tab;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.example.infosys1d_amigoproject.models.users_display;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Arrays;
import java.util.UUID;


// the main concept of this activity is that we want to show the information that is
// currently already set by the creator
// hence we need to pull information from firebase first to allow the creator
// to see the information that he needs to edit instead of writing everything over again
// after that the editing function are simply edit text views to allow the creator to edit the relevant information
// much of the remaining code is similar to the "Create Project" activity that we have explained in greater detail.

public class EditPojects extends AppCompatActivity {

    ImageView imageView;
    Button button,create_project;
    private static final int PICK_IMAGE = 1;
    public Uri imageUri;
    Uri downloadUrl = null;
    FirebaseStorage storage;
    StorageReference storageRef;
    TextInputLayout textInputLayout,textInputLayoutdescrip;
    String randomKey;
    ArrayList<String> category;
    ArrayList<String> skills;

    DatabaseReference myref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userref = FirebaseDatabase.getInstance().getReference();

    FirebaseMethods firebaseMethods;
    Project project;



    private Context mcontext;
    private ChipGroup mfilters;
    private ChipGroup categoryChipGroup;
    private ArrayList<String> selectedChipData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pojects);
        imageView = findViewById(R.id.editPimageview2);
        textInputLayout = findViewById(R.id.editPtextInputLayout);
        textInputLayoutdescrip = findViewById(R.id.editPtextInputLayout_description);
        button = findViewById(R.id.editPbutton);
        create_project = findViewById(R.id.editPbutton_create_project);
        firebaseMethods = new FirebaseMethods(getApplicationContext());
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        selectedChipData = new ArrayList<>();


        mcontext = getApplicationContext();
        String[] filterList = mcontext.getResources().getStringArray(R.array.skills_list);

        mfilters = findViewById(R.id.editPfilterChipGroup);

        LayoutInflater inflater_0 = LayoutInflater.from(mcontext);
        for(String text: filterList){
            Chip newChip = (Chip) inflater_0.inflate(R.layout.chip_filter,null,false);
            newChip.setText(text);
            mfilters.addView(newChip);}

        String[] projectCategories = mcontext.getResources().getStringArray(R.array.category_list);

        categoryChipGroup = findViewById(R.id.categoryChipGroup);
        LayoutInflater inflater_1 = LayoutInflater.from(mcontext);
        for(String text: projectCategories){
            Chip newChip = (Chip) inflater_1.inflate(R.layout.chip_filter,null,false);
            newChip.setText(text);
            categoryChipGroup.addView(newChip);}


        Intent intent = getIntent();
        String project_id = intent.getStringExtra("Project ID");
        myref = FirebaseDatabase.getInstance().getReference().child("Projects").child(project_id);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                project = snapshot.getValue(Project.class);
                Picasso.get().load(project.getThumbnail()).into(imageView); // external api to load an image into the project thumbnail into the imageview.
                textInputLayout.getEditText().setText(project.getProjectitle());
                textInputLayoutdescrip.getEditText().setText(project.getProjectdescription());
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

                for (int i = 0; i < filterList.length; i++){
                    Chip chip = (Chip) mfilters.getChildAt(i);
                    if ( project.getSkillsrequired().contains(chip.getText().toString())){
                        chip.setChecked(true);
                    }

                }

                categoryChipGroup.getCheckedChipIds();
                for(int i = 0; i<categoryChipGroup.getChildCount(); i++){
                    Chip chip = (Chip) categoryChipGroup.getChildAt(i);
                    if ( project.getCategory().contains(chip.getText().toString())){
                        chip.setChecked(true);
                    }
                }

                userref = FirebaseDatabase.getInstance().getReference().child("users_display").child(project.getCreatedby());
                userref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users_display user = snapshot.getValue(users_display.class);
                        Picasso.get().load(project.getThumbnail()).into(imageView); // external api to load an image into the project thumbnail into the imageview.
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        create_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                myref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        project = snapshot.getValue(Project.class);
                        if (textInputLayout.getEditText().getText().toString().equals(project.getProjectitle())){
                            Toast.makeText(EditPojects.this, "THIS IS THE SAME/FILL IN THE BLANKS!", Toast.LENGTH_SHORT).show();}
                        else if (textInputLayoutdescrip.getEditText().getText().toString().equals(project.getProjectdescription())) {Toast.makeText(EditPojects.this, "THIS IS THE SAME/FILL IN THE BLANKS!", Toast.LENGTH_SHORT).show();}
                        else if (downloadUrl == null){Toast.makeText(EditPojects.this, "Upload Picture!", Toast.LENGTH_SHORT).show();}
                        else {
                            selectedChipData.clear();
                            for(int i = 0; i<mfilters.getChildCount(); i++){
                                Chip chip = (Chip)mfilters.getChildAt(i);
                                if(chip.isChecked()){
                                    selectedChipData.add(chip.getText().toString());
                                }
                            }

                            categoryChipGroup.getCheckedChipIds();
                            for(int i = 0; i<categoryChipGroup.getChildCount(); i++){
                                Chip chip = (Chip)categoryChipGroup.getChildAt(i);
                                if(chip.isChecked()){
                                    category.add(chip.getText().toString());
                                }
                            }

                            Project new_proj = new Project(downloadUrl.toString(), textInputLayout.getEditText().getText().toString(),
                                    textInputLayoutdescrip.getEditText().getText().toString(),
                                    selectedChipData, new ArrayList<String>(Arrays.asList(firebaseMethods.getUserID())), category,
                                    firebaseMethods.getUserID(), project_id);
                            FirebaseDatabase.getInstance().getReference().child("Projects").child(project_id).setValue(new_proj);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            imageView.setImageURI(imageUri);
            uploadpicture();
        }
        else{
            imageView.setImageResource(R.drawable.ic_android);
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
//                        Snackbar.make(findViewById(R.id.upload), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                    };
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
}