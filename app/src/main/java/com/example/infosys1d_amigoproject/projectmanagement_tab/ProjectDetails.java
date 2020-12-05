package com.example.infosys1d_amigoproject.projectmanagement_tab;

import android.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.SearchFragment;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.adapter.ApplicantAdapter;
import com.example.infosys1d_amigoproject.adapter.MemberAdapter;
import com.example.infosys1d_amigoproject.adapter.UserAdapter;
import com.example.infosys1d_amigoproject.chat_tab.MessageActivity;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.models.users_display;
import com.example.infosys1d_amigoproject.models.users_private;
import com.example.infosys1d_amigoproject.profilemanagement_tab.profileactivity;
import com.example.infosys1d_amigoproject.profilemanagement_tab.viewprofilefragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ProjectDetails extends AppCompatActivity {
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    DatabaseReference myref,userref_display;
    ImageView imageView, createdby_pic;
    TextView createdby_text,project_description,projecttitle, applicantstitle ,nocurrentapplicants, memberstitle;
    Project project;
    Button applytoJoin, back,delete, cancelapply;
    ImageButton imageButton, clicktoChat;
    ChipGroup skillsrequired;
    RecyclerView applicantrecycler, membersrecycler;
    Context mcontext = ProjectDetails.this;
    FirebaseUser muser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id_creator;
    ConstraintLayout applicantsconstraint;

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
        applicantrecycler = findViewById(R.id.ApplicantRecycler);
        applicantstitle = findViewById(R.id.applicants_title);
        delete = findViewById(R.id.delete);
        nocurrentapplicants = findViewById(R.id.no_applicants);
        membersrecycler = findViewById(R.id.MembersRecycler);
        memberstitle = findViewById(R.id.MembersTitle);
        applicantsconstraint = findViewById(R.id.applicantconstraint);
        applytoJoin = findViewById(R.id.applytoJoin);



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
                skillsrequired.removeAllViews();
                for(String text: project.getSkillsrequired()){
                    Chip newchip = (Chip) inflater.inflate(R.layout.chip_item,null,false);
                    newchip.setText(text);
                    skillsrequired.addView(newchip);}
                if (!project.getCreatedby().equals(firebaseMethods.getUserID())){
                    imageButton.setVisibility(View.GONE);
                    applicantstitle.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                }
                else {
                    clicktoChat.setVisibility(View.GONE);
                }
                ArrayList<users_display> mUserlist = new ArrayList<>();
                ArrayList<users_display> mMemberslist = new ArrayList<>();
                ArrayList<String> mUserIDs = new ArrayList<>();
                ArrayList<String> mUserIDs_members = new ArrayList<>();
                FirebaseUser currenuser = FirebaseAuth.getInstance().getCurrentUser();
                if (project.getApplicantsinProject().contains(currenuser.getUid())){
                    applytoJoin.setVisibility(View.GONE);
                }
                else {
                    cancelapply.setVisibility(View.GONE);
                }
                if(project.getUsersinProject().contains(currenuser.getUid())){
                    cancelapply.setVisibility(View.GONE);
                    applytoJoin.setVisibility(View.GONE);
                }
                DatabaseReference mref = FirebaseDatabase.getInstance().getReference().child("users_display");

                mref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mMemberslist.clear();
                        mUserIDs.clear();
                        mUserlist.clear();
                        mUserIDs_members.clear();

                        for(DataSnapshot ds: snapshot.getChildren()){


                            users_display currentiter = ds.getValue(users_display.class);
                            if(project.getApplicantsinProject().contains(ds.getKey())){
                                if(!mUserlist.contains(currentiter)){
                                    mUserlist.add(currentiter);}
                                    mUserIDs.add(ds.getKey());
                            }

                            if(project.getUsersinProject().contains(ds.getKey())){
                                if(!mMemberslist.contains(currentiter)){
                                    mMemberslist.add(currentiter);
                                    mUserIDs_members.add(ds.getKey());
                                }
                            }


                        }
                        System.out.println(mMemberslist);
                        membersrecycler.setVisibility(View.VISIBLE);
                        MemberAdapter memberAdapter = new MemberAdapter(mcontext, mMemberslist, project, mUserIDs_members);
                        membersrecycler.setLayoutManager(new LinearLayoutManager(mcontext));
                        membersrecycler.setAdapter(memberAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });






                userref_display = FirebaseDatabase.getInstance().getReference().child("users_display").child(project.getCreatedby());
                userref_display.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        users_display user = snapshot.getValue(users_display.class);
                        user_id_creator = snapshot.getKey();

                        if(user.getProfile_picture().equals("")){
                            createdby_pic.setImageResource(R.mipmap.ic_launcher_round);


                        }else{
                        Picasso.get().load(user.getProfile_picture()).into(createdby_pic);}
                        createdby_text.setText(user.getName());


                        applicantsconstraint.setVisibility(View.GONE);
                        applicantstitle.setVisibility(View.GONE);
                        nocurrentapplicants.setVisibility(View.GONE);


                        if(muser.getUid().equals(project.getCreatedby())){
                            applytoJoin.setVisibility(View.GONE);
                            applicantsconstraint.setVisibility(View.VISIBLE);
                            applicantstitle.setVisibility(View.VISIBLE);
                            nocurrentapplicants.setVisibility(View.VISIBLE);
                            if(!mUserlist.isEmpty()){
                                applicantrecycler.setVisibility(View.VISIBLE);
                                nocurrentapplicants.setVisibility(View.GONE);
                                ApplicantAdapter newadapter = new ApplicantAdapter(mcontext, mUserlist,mUserIDs,project);
                                applicantrecycler.setLayoutManager(new LinearLayoutManager(mcontext));
                                applicantrecycler.setAdapter(newadapter);
                            }


                        }
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
        cancelapply = findViewById(R.id.cancelapply);
        cancelapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Projects").child(project.getProjectID());
                List<String> applicantlst = project.getApplicantsinProject();
                if (applicantlst.contains(muser.getUid())){
                    applicantlst.remove(muser.getUid());
                    HashMap<String, Object> hashmap = new HashMap<>();
                    hashmap.put("applicantsinProject", applicantlst);
                    mref.updateChildren(hashmap);
                    Snackbar.make(findViewById(R.id.projdetails), "Application Cancelled!", Snackbar.LENGTH_LONG).show();
                    applytoJoin.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(getApplicationContext(),"You did not apply for this project",Toast.LENGTH_LONG).show();
                }
            }
        });

        applytoJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Projects").child(project.getProjectID());
                List<String> applicantlst = project.getApplicantsinProject();
                if (!applicantlst.contains(muser.getUid())){
                    applicantlst.add(muser.getUid());
                    HashMap<String, Object> hashmap = new HashMap<>();
                    hashmap.put("applicantsinProject", applicantlst);
                    mref.updateChildren(hashmap);
                    Snackbar.make(findViewById(R.id.projdetails), "Project applied!", Snackbar.LENGTH_LONG).show();
                    cancelapply.setVisibility(View.VISIBLE);
                }
                else{
                    Toast T = Toast.makeText(getApplicationContext(),"Project has been applied!",Toast.LENGTH_SHORT);
                    T.setGravity(Gravity.CENTER_VERTICAL,0,-60);
                    T.show();
                }
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

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Projects").child(project.getProjectID());
                mref.removeValue();
                finish();
            }
        });

        createdby_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent INT = new Intent(ProjectDetails.this, profileactivity.class);
                INT.putExtra("Calling Activity" , "Message Activity");
                INT.putExtra("Intent User" , user_id_creator);
                startActivity(INT);
            }
        });
        }
    }