package com.example.infosys1d_amigoproject.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.chat_tab.MessageActivity;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.models.users_display;
import com.example.infosys1d_amigoproject.models.users_private;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ApplicantAdapter extends RecyclerView.Adapter<ApplicantAdapter.Viewholder> {

    private Context mcontext;
    private ArrayList<users_display> mUsers;
    DatabaseReference myref;


    public ApplicantAdapter(Context mcontext, ArrayList<users_display> mUsers){
        this.mUsers = mUsers;
        this.mcontext = mcontext;
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profile_image;
        public ImageButton accept, reject;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            accept = itemView.findViewById(R.id.applicant_item_accept);
            reject = itemView.findViewById(R.id.applicant_item_reject);
            username = itemView.findViewById(R.id.Chat_username);
            profile_image = itemView.findViewById(R.id.Chat_Profile_Image);
        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.useritem_applicant, parent, false);


        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        users_display user = mUsers.get(position);


        holder.username.setText(user.getName());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        if(user.getProfile_picture().equals("none")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }
        else{
            Picasso.get().load(user.getProfile_picture()).into(holder.profile_image);
        }


    }



    @Override
    public int getItemCount() {
        return mUsers.size();
    }


    }
