package edu.uga.cs.letsjitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatActivity extends AppCompatActivity {
    private CircleImageView profileImage;
    private TextView groupname, sendMessageText;
    private Button sendButton;
    private FirebaseUser currentUser;
    private DatabaseReference myDatabase, userReference;
    private Intent intent;
    private String groupName, userName; //for intent
    private GroupChatAdapterList groupChatAdapter;
    private Group group;
    private List<Chat> myChat;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        recyclerView = findViewById(R.id.groupRecycle); //recycler specifically for messages
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true); //helps with scrolling when you enter a text
        recyclerView.setLayoutManager(linearLayoutManager);

        currentUser = FirebaseAuth.getInstance().getCurrentUser(); //get current user and all properties with .
        userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()); //get reference of current user
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("username")){
                    userName = dataSnapshot.child("username").getValue().toString(); //get current username
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        profileImage = (CircleImageView) findViewById(R.id.groupImage);
        groupname = (TextView) findViewById(R.id.groupName);
        sendMessageText = (TextView) findViewById(R.id.groupMessageEditText);
        sendButton = (Button) findViewById(R.id.groupSendButton);
        intent = getIntent();
        groupName = intent.getStringExtra("groupName"); //get userID from intent in ContactAdapter
//        System.out.println("Intent ID: "+userId);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = sendMessageText.getText().toString();
                if(message != null || !message.equals("")){
                    setSendMessage(currentUser.getUid(),userName, message); //current user(user.getUid() sending to other user (userId)
                }else{
                    Toast.makeText(GroupChatActivity.this, "Can't send an empty message", Toast.LENGTH_SHORT).show();
                }
                sendMessageText.setText(""); //set textfield back to empty
            }
        });

        myDatabase = FirebaseDatabase.getInstance().getReference("Groups"); //reference the Group
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(groupName)){ //if the Groups root contains the groupName from the intent
                    groupname.setText(groupName);
                }
                displayMessages(currentUser.getUid()); //display messages of all  users
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /**
     * This message sets the messages to be sent to the receiver by the sender
     * @param sender the sender's id
     * @param name the sender's name
     * @param message the message to be sent
     */
    private void setSendMessage(String sender, String name, String message){
        myDatabase = FirebaseDatabase.getInstance().getReference("Groups").child(groupName); //send to specific group in database
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("sendername", name);
        hashMap.put("message", message);
        myDatabase.push().setValue(hashMap); //create a Chat's database instance and then set values appened to chat database
        //the Chats root database acts like a list, so when I do myDatabase.push(), I'm added new messages to the list
    }

    /**
     * This method displays the messages on the screen
     * @param myID the current user's id
     */
    private void displayMessages(final String myID){
        myChat = new ArrayList<>();
        myDatabase = FirebaseDatabase.getInstance().getReference("Groups").child(groupName); //reference Chats database to display all messages
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myChat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                   // Chat chat = snapshot.getValue(Chat.class);
                    Chat chat = new Chat();
                    chat.setSender(snapshot.child("sender").getValue().toString());
                    chat.setSenderName(snapshot.child("sendername").getValue().toString());
                    chat.setMessage(snapshot.child("message").getValue().toString());
                    //this is for the receiver and sender messages display
                    if(chat.getSender().equals(myID) || !chat.getSender().equals(myID)){ //if sender is current user or not
                        myChat.add(chat); //add current user text into array
                    }
                    groupChatAdapter = new GroupChatAdapterList(GroupChatActivity.this, myChat);
                    recyclerView.setAdapter(groupChatAdapter);
                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });

    }

}
