package edu.uga.cs.letsjitter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private FirebaseUser firebaseUser;
    private FirebaseAuth authentication;
    private DatabaseReference myDatabase;
    private ContactAdapter contactAdapter;
    private List<ContactUser> myUser;
    private ContactUser user;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication = FirebaseAuth.getInstance();
        firebaseUser = authentication.getCurrentUser();
        myDatabase = FirebaseDatabase.getInstance().getReference("Users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        myUser = new ArrayList<ContactUser>();
        readUsers(); //read users from database
        return view;
    }
    private void readUsers(){
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { // the problem has to be here. Maybe the logic
                myUser.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    user = new ContactUser();
                    user = snapshot.getValue(ContactUser.class);
                    user.setUserID(snapshot.child("id").getValue().toString());
                   // System.out.println("Children: "+user.toString());
                    myUser.add(user);
                }
                contactAdapter = new ContactAdapter(getContext(), myUser);
                recyclerView.setAdapter(contactAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
