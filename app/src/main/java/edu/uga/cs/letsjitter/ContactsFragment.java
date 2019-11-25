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
import java.util.Collections;
import java.util.List;

public class ContactsFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private FirebaseUser firebaseUser;
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
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        myDatabase = FirebaseDatabase.getInstance().getReference("Users");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contacts, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_Contacts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); //recyclerView will be wraping view in linear layout style
        myUser = new ArrayList<ContactUser>(); //store all users in ContactUser array
        readUsers(); //read users from database
        return view;
    }
    private void readUsers(){
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    myUser.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//root is users so we want to loop through all the users
                        user = new ContactUser(); //create a new user object everytime we find a new child
                        //user = snapshot.getValue(ContactUser.class);
                        user.setUserID(snapshot.child("id").getValue().toString());
                        user.setUsername(snapshot.child("username").getValue().toString());
                        user.setImageURL(snapshot.child("imageURL").getValue().toString());
                        if (user.getUserID().equals(firebaseUser.getUid())) { //if child user id equals current user id skip
                            continue; //skip adding current user to contact's list
                        }
                        // System.out.println("Children: "+user.toString());
                        myUser.add(user);
                    }
                    contactAdapter = new ContactAdapter(getContext(), myUser);
                    recyclerView.setAdapter(contactAdapter);
                }catch (Exception ex){ //catch that null error we've been getting
                    ex.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
