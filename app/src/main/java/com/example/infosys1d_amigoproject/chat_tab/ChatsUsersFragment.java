package com.example.infosys1d_amigoproject.chat_tab;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// Nested child fragment of ChatsFragment TabLayout.
// All users tab. Populates with all users to allow anyone to contact anyone

public class ChatsUsersFragment extends Fragment {

    private static final String TAG = "ChatsUsersFragment";

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private ArrayList<Userdataretrieval> mUsers;
    EditText searchbar;

    private Context mcontext = getContext();

    FirebaseMethods firebaseMethods;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        firebaseMethods = new FirebaseMethods(mcontext);
        String currentuser = firebaseMethods.getUserID();



        recyclerView = view.findViewById(R.id.userrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mcontext));
        mUsers = new ArrayList<>();
        readUsers(currentuser);


        searchbar = view.findViewById(R.id.searchusersinuserfragment);

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchusers(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }

    private void searchusers(String s) {
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseMethods firebaseMethods = new FirebaseMethods(mcontext);
        Query query = FirebaseDatabase.getInstance().getReference("users_display").orderByChild("name")
                .startAt(s).endAt(s+"\uf8ff");

        DatabaseReference mref = FirebaseDatabase.getInstance().getReference();
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userAdapter=new UserAdapter(getContext(), mUsers, getActivity(), false);
                recyclerView.setAdapter(userAdapter);
                mUsers.clear();

                    ArrayList<Userdataretrieval> list = firebaseMethods.getuserlist(snapshot);
                    for(Userdataretrieval userdataretrieval : list){
                        users_private userprivatedata = userdataretrieval.getUsersprivate();
                        users_display userdisplaydata = userdataretrieval.getUsersdisplay();

                        assert userdataretrieval!=null;
                        assert fuser!=null;
                        if(!userprivatedata.getUser_id().equals(fuser.getUid())){
                            mUsers.add(userdataretrieval);

                        }

                }
                userAdapter.notifyDataSetChanged();
                userAdapter.setmUsersAll(mUsers);
                userAdapter.getFilter().filter(s);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void readUsers(final String currentuser){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(searchbar.getText().toString().equals("")){
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

                }}


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}