package com.example.infosys1d_amigoproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

//Adapter used for populating the message activity when a user clicks into a chat

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.Viewholder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private Context mcontext;
    private ArrayList<Chat> mChat;
    private String imageurlleft;
    private String imageurlright;
    FirebaseUser fuser;

    public messageAdapter(Context mcontext, ArrayList<Chat> mChat, String imageurlleft, String imageurlright) {
        this.mcontext = mcontext;
        this.mChat = mChat;
        this.imageurlleft = imageurlleft;
        this.imageurlright = imageurlright;
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;

        public Viewholder(@NonNull View itemView) {
            super(itemView);


            show_message = itemView.findViewById(R.id.chatmessagemessagecontent);
            profile_image = itemView.findViewById(R.id.chatmessageprofilepic);
        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right, parent, false);
            return new Viewholder(view);
        }
        else{
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left, parent, false);
            return new Viewholder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());


        if(holder.getItemViewType() == MSG_TYPE_RIGHT) {
            if (imageurlright.equals(null)) {
                holder.profile_image.setImageResource(R.mipmap.ic_launcher_round);
            } else {
                Glide.with(mcontext).load(imageurlright).into(holder.profile_image);// external api to load an image into the project thumbnail into the imageview.
            }
        }else{
            if (imageurlleft.equals(null)) {
                holder.profile_image.setImageResource(R.mipmap.ic_launcher_round);
            } else {
                Glide.with(mcontext).load(imageurlleft).into(holder.profile_image);
            }
        }




    }


    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        if(mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }
        else{
            return MSG_TYPE_LEFT;
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }
}