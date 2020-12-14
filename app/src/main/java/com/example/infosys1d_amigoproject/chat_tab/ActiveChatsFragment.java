package com.example.infosys1d_amigoproject.chat_tab;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.adapter.UserAdapter;
import com.example.infosys1d_amigoproject.models.Chat;
import com.example.infosys1d_amigoproject.models.Userdataretrieval;
import com.example.infosys1d_amigoproject.models.users_display;
import com.example.infosys1d_amigoproject.models.users_private;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// Nested child fragment of ChatsFragment TabLayout.
// Active chats tab. Will populate if there is an active chat with another user, else, will be empty

public class ActiveChatsFragment extends Fragment {

    private static final String TAG = "ActiveChatsFragment";

    private RecyclerView activechats;
    private UserAdapter userAdapter;

    private ArrayList<Userdataretrieval> mUsers;

    private FirebaseUser fuser;
    private DatabaseReference reference;

    private ArrayList<String> userslist;

    private Context mcontext = getContext();
    private TextView noChatsError;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);


        //Widgets instantiation
        noChatsError = view.findViewById(R.id.noChats);
        activechats = view.findViewById(R.id.activechatsrecycler);
        activechats.setHasFixedSize(true);
        activechats.setLayoutManager(new LinearLayoutManager(mcontext));


        //Firebase Instantiation
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        //Empty Arraylist instantiated. To hold all active chats.
        userslist = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    Chat chat = (Chat)ds.getValue(Chat.class);
                    if(chat.getSender().equals(fuser.getUid())&& !userslist.contains(chat.getReceiver())){
                        userslist.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(fuser.getUid())&& !userslist.contains(chat.getSender())){
                        userslist.add(chat.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    //Parse chats to check if the current user is either sender/receiver
    private void readChats(){
        Log.d(TAG, "readChats: init read chats");
        mUsers = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();

                FirebaseMethods firebaseMethods = new FirebaseMethods(mcontext);
                ArrayList<Userdataretrieval> list = firebaseMethods.getuserlist(snapshot);

                for(Userdataretrieval data: list){
                    users_display userdisplay = data.getUsersdisplay();
                    users_private userprivate = data.getUsersprivate();
                    //To display 1 user from the chat
                    for(String id: userslist){
                        if(userprivate.getUser_id().equals(id)){
                            mUsers.add(data);
                        }
                    }
                }

                userAdapter = new UserAdapter(getContext(),mUsers, getActivity(), true);
                activechats.setAdapter(userAdapter);
                }

            

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d(TAG, "readChats: end read chats");

    }

}