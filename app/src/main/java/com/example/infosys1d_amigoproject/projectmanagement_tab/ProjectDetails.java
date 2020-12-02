package com.example.infosys1d_amigoproject.projectmanagement_tab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.chat_tab.MessageActivity;
import com.example.infosys1d_amigoproject.models.users_display;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProjectDetails extends AppCompatActivity {
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    DatabaseReference myref,userref;
    ImageView imageView, createdby_pic;
    TextView createdby_text,project_description,projecttitle;
    Project project;
    Button applytoJoin, back;
    ImageButton imageButton, clicktoChat;
    ChipGroup skillsrequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);
        mCollapsingToolbarLayout = findViewById(R.id.collapse);
        createdby_text = findViewById(R.id.projectCreator);
        project_description = findViewById(R.id.projectDescription);
        createdby_pic = findViewById(R.id.createdby_pic);
        projecttitle = findViewById(R.id.projecttitle);
        imageButton = findViewById(R.id.editprojectbutton);
        skillsrequired = findViewById(R.id.projectskillchipsgroup);

        FirebaseMethods firebaseMethods = new FirebaseMethods(getApplicationContext());
        imageView = findViewById(R.id.project_picture);
        Intent intent = getIntent();
        String project_id = intent.getStringExtra("ProjectID");
        myref = FirebaseDatabase.getInstance().getReference().child("Projects").child(project_id);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                project = snapshot.getValue(Project.class);
                Picasso.get().load(project.getThumbnail()).into(imageView);
                mCollapsingToolbarLayout.setTitle(project.getProjectitle());
                project_description.setText(project.getProjectdescription());
                projecttitle.setText(project.getProjectitle());
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

                for(String text: project.getSkillsrequired()){
                    Chip newchip = (Chip) inflater.inflate(R.layout.chip_item,null,false);
                    newchip.setText(text);
                    skillsrequired.addView(newchip);}
                if (!project.getCreatedby().equals(firebaseMethods.getUserID())){
                    System.out.println(project.getCreatedby() + "1234567");
                    System.out.println(firebaseMethods.getUserID() + "1234567");
                    imageButton.setVisibility(View.GONE);

                }
                else {
                    clicktoChat.setVisibility(View.GONE);
                }

                userref = FirebaseDatabase.getInstance().getReference().child("users_display").child(project.getCreatedby());
                userref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users_display user = snapshot.getValue(users_display.class);
                        Picasso.get().load(user.getProfile_picture()).into(createdby_pic);
                        createdby_text.setText(user.getName());
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
        mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.theme_expanded);
        mCollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.theme_collapsed);



        clicktoChat = findViewById(R.id.chatnow);
        clicktoChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent to chat with project creator
                Intent intent = new Intent(v.getContext(), MessageActivity.class);
                intent.putExtra("userid", project.getCreatedby());
                startActivity(intent);
            }
        });
        applytoJoin = findViewById(R.id.applytoJoin);
        applytoJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // intent to project application fragment OR fillable dialogue
            }
        });

        back = findViewById(R.id.back_from_project_details);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageButton = findViewById(R.id.editprojectbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditPojects.class);
                intent.putExtra("Project ID", project_id);
                startActivity(intent);
            }
        });


        }
    }