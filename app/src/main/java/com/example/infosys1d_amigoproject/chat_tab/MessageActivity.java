package com.example.infosys1d_amigoproject.chat_tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.adapter.messageAdapter;
import com.example.infosys1d_amigoproject.models.Chat;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.models.users_display;
import com.example.infosys1d_amigoproject.models.users_private;
import com.example.infosys1d_amigoproject.profilemanagement_tab.editprofilefragment;
import com.example.infosys1d_amigoproject.profilemanagement_tab.profileactivity;
import com.example.infosys1d_amigoproject.profilemanagement_tab.profilefragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class MessageActivity extends AppCompatActivity {

    TextView personyoutalkingto;
    EditText chatMessage;
    ImageButton sendbutton, backbutton;
    RecyclerView pastmessages;
    ImageView profilepic, icon_on, icon_off;



    FirebaseUser fuser;
    DatabaseReference mref;

    Context mcontext = MessageActivity.this;

    Intent intent;

    messageAdapter messageAdapter;
    ArrayList<Chat> mChat;
    Userdataretrieval intentuser;
    String imgurlright;
    String imgurlleft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        personyoutalkingto = findViewById(R.id.Personyoutexting);
        chatMessage = findViewById(R.id.chatmessage);
        sendbutton = findViewById(R.id.buttonsend);
        backbutton = findViewById(R.id.chatbackbutton);
        pastmessages = findViewById(R.id.pastchatmessages);
        profilepic = findViewById(R.id.personyoutextingprofile);
        icon_on = findViewById(R.id.statusonlinemessage);
        icon_off = findViewById(R.id.statusofflinemessage);

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent INT = new Intent(MessageActivity.this, profileactivity.class);
                INT.putExtra("Calling Activity" , "Message Activity");
                final String getID = intent.getStringExtra("userid");
                INT.putExtra("Intent User" , getID);
                startActivity(INT);

            }
        });

        pastmessages.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
        linearLayoutManager.setStackFromEnd(true);
        pastmessages.setLayoutManager(linearLayoutManager);

        intent =getIntent();
        final String getID = intent.getStringExtra("userid");


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = chatMessage.getText().toString();
                if(!msg.equals("")){
                    SendMessage(fuser.getUid(), getID, msg);
                    chatMessage.setText("");
                }
                else{
                    Toast.makeText(mcontext, "You cannot send empty messages", Toast.LENGTH_SHORT).show();
                }
            }
        });


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        mref = FirebaseDatabase.getInstance().getReference("users_display");

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    if(ds.getKey().equals(getID)) {
                        users_display user = ds.getValue(users_display.class);
                        personyoutalkingto.setText(user.getName());
                        if (user.getProfile_picture().equals("")) {
                            profilepic.setImageResource(R.mipmap.ic_launcher_round);
                        } else {
                            Picasso.get().load(user.getProfile_picture()).into(profilepic);
                            imgurlleft = user.getProfile_picture();
                        }

                        if (user.getStatus().equals("online")) {
                            icon_on.setVisibility(View.VISIBLE);
                            icon_off.setVisibility(View.GONE);
                        } else {
                            icon_off.setVisibility(View.VISIBLE);
                            icon_on.setVisibility(View.GONE);
                        }
                    }
                    else if(ds.getKey().equals(fuser.getUid())){
                        imgurlright = ds.getValue(users_display.class).getProfile_picture();
                    }
                }
                ReadMessages(fuser.getUid(), getID, imgurlleft, imgurlright);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void SendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object>hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);

    }


    private void ReadMessages(final String myid, final String userid, final String imgurl, final String imgurlright){
        mChat = new ArrayList<>();

        mref = FirebaseDatabase.getInstance().getReference("Chats");
        FirebaseUser curruser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference cref = FirebaseDatabase.getInstance().getReference("users_display");

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChat.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Chat chat = (Chat)ds.getValue(Chat.class);
                    if((chat.getReceiver().equals(myid)&& chat.getSender().equals(userid)) || (chat.getReceiver().equals(userid)&& chat.getSender().equals(myid)) ){
                        mChat.add(chat);
                    }
                }
                messageAdapter = new messageAdapter(mcontext, mChat, imgurl, imgurlright);
                pastmessages.setAdapter(messageAdapter);
                pastmessages.scrollToPosition(messageAdapter.getItemCount()-1);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void status(String status){
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mref = FirebaseDatabase.getInstance().getReference("users_display").child(fuser.getUid());
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("status", status);

        mref.updateChildren(hashmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}