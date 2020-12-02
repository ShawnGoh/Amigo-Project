package com.example.infosys1d_amigoproject.chat_tab;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.infosys1d_amigoproject.R;
import com.example.infosys1d_amigoproject.Utils.FirebaseMethods;
import com.example.infosys1d_amigoproject.adapter.UserAdapter;
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

public class ChatsUsersFragment extends Fragment {

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private ArrayList<Userdataretrieval> mUsers;

    private Context mcontext = getContext();

    FirebaseMethods firebaseMethods;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        firebaseMethods = new FirebaseMethods(mcontext);
        String currentuser = firebaseMethods.getUserID();



        System.out.println(currentuser);

        recyclerView = view.findViewById(R.id.userrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));

        mUsers = new ArrayList<>();


        readUsers(currentuser);







        return view;
    }

    public void readUsers(final String currentuser){

        System.out.println("init read users");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                    ArrayList<Userdataretrieval> list = firebaseMethods.getuserlist(snapshot);

                    for(Userdataretrieval data: list){
                        users_display userdisplay = data.getUsersdisplay();
                        users_private userprivate = data.getUsersprivate();

                        if(!userprivate.getUser_id().equals(fuser.getUid())){
                            mUsers.add(data);}
                    }
                    userAdapter = new UserAdapter(getContext(),mUsers, getActivity(), false);
                    recyclerView.setAdapter(userAdapter);

                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}