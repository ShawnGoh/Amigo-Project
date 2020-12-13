package com.example.infosys1d_amigoproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.models.users_display;
import com.example.infosys1d_amigoproject.profilemanagement_tab.profileactivity;
import com.example.infosys1d_amigoproject.projectmanagement_tab.Project;
import com.example.infosys1d_amigoproject.projectmanagement_tab.ProjectDetails;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.Viewholder> {

    private Context mcontext;
    private ArrayList<users_display> mUsers_display;
    private ArrayList<String> membersIDs;
    DatabaseReference myref;
    Project project;


    public MemberAdapter(Context mcontext, ArrayList<users_display> mUsers_display,  Project project, ArrayList<String> membersIDs){
        this.mUsers_display = mUsers_display;
        this.mcontext = mcontext;
        this.project = project;
        this.membersIDs = membersIDs;
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        public ImageButton remove;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.Chat_username);
            profile_image = itemView.findViewById(R.id.Chat_Profile_Image);
            remove = itemView.findViewById(R.id.remove);

        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.useritem_member, parent, false);
        System.out.println("member view inflated");

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        users_display user = mUsers_display.get(position);
        System.out.println(user.getName());
        holder.username.setText(user.getName());
        myref = FirebaseDatabase.getInstance().getReference("Projects").child(project.getProjectID());

        if(user.getProfile_picture().equals("")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Picasso.get().load(user.getProfile_picture()).into(holder.profile_image);  // external api to load an image into the project thumbnail into the imageview.
        }
        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent INT = new Intent(v.getContext(), profileactivity.class);
                INT.putExtra("Calling Activity" , "Message Activity");
                INT.putExtra("Intent User" , membersIDs.get(position));
                v.getContext().startActivity(INT);
            }
        });
        if (!project.getCreatedby().equals(FirebaseAuth.getInstance().getUid()) || membersIDs.get(position).equals(FirebaseAuth.getInstance().getUid())){
            holder.remove.setVisibility(View.GONE);
        }
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mref = FirebaseDatabase.getInstance().getReference("Projects").child(project.getProjectID());
                List<String> members = project.getUsersinProject();
                if (members.contains(membersIDs.get(position))) {
                    members.remove(membersIDs.get(position));
                    HashMap<String, Object> hashmap = new HashMap<>();
                    hashmap.put("usersinProject", members);
                    mref.updateChildren(hashmap);
                    Snackbar.make(holder.itemView.getRootView().findViewById(R.id.projdetails), user.getName() + " removed from project", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return mUsers_display.size();
    }


    }
