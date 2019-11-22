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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private FirebaseUser firebaseUser;
    private FirebaseAuth authentication;
    private DatabaseReference myDatabase;
    private List<Group> myGroupsList;
    private GroupAdapter groupAdapter;
    private Group myGroup;

    public GroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authentication = FirebaseAuth.getInstance();
        firebaseUser = authentication.getCurrentUser();
        myDatabase = FirebaseDatabase.getInstance().getReference("Groups"); //we want to reference Groups in the database
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_group, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_Groups);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); //recyclerView will be wraping view in linear layout style
        myGroupsList = new ArrayList<Group>(); //store all users in ContactUser array
        readGroupFromDatabase(); //read users from database
        return view;
    }

    public void readGroupFromDatabase(){
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myGroupsList.clear(); //clear everytime
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){//root is Groups so we want to loop through all the Groups
                    myGroup = new Group(); //create a new Group object everytime we find a new child
                    //user = snapshot.getValue(ContactUser.class);
                    myGroup.setGroupName(snapshot.getKey()); //set the groupName for this object at this point in time
                    // System.out.println("Children: "+user.toString());
                    myGroupsList.add(myGroup);
                }
                groupAdapter = new GroupAdapter(getContext(), myGroupsList);
                recyclerView.setAdapter(groupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
