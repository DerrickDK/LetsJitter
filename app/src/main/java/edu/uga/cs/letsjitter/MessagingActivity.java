package edu.uga.cs.letsjitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

public class MessagingActivity extends AppCompatActivity {
    private CircleImageView profileImage;
    private TextView username, sendMessageText;
    private Button sendButton;
    private FirebaseUser currentUser;
    private FirebaseAuth authentication;
    private DatabaseReference myDatabase, userReference;
    private Intent intent;
    private String userId; //for intent
    private String currentUserName; //for intent
    private ChatAdapter chatAdapter;
    private ContactUser receiver;
    private List<Chat> myChat;
    private RecyclerView recyclerView;

    /**
     * This is the main method that gets the current user information and messaging information to be displayed on the screen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        recyclerView = findViewById(R.id.messageRecycle); //recycler specifically for messages
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true); //helps with scrolling when you enter a text
        recyclerView.setLayoutManager(linearLayoutManager);

        profileImage = (CircleImageView) findViewById(R.id.profileimage);
        username = (TextView) findViewById(R.id.userName);
        sendMessageText = (TextView) findViewById(R.id.messageEditText);
        sendButton = (Button) findViewById(R.id.sendButton);
        authentication = FirebaseAuth.getInstance();
        currentUser = authentication.getCurrentUser();
        userReference = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()); //get reference of current user
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("username")){
                    currentUserName = dataSnapshot.child("username").getValue().toString(); //get current username
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        intent = getIntent();
        userId = intent.getStringExtra("userID"); //get userID from intent in ContactAdapter
//        System.out.println("Intent ID: "+userId);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = sendMessageText.getText().toString();
                if(message != null || !message.equals("")){
                    setSendMessage(currentUser.getUid(), currentUserName, userId, message); //current user(user.getUid() sending to other user (userId)
                }else{
                    Toast.makeText(MessagingActivity.this, "Can't send an empty message", Toast.LENGTH_SHORT).show();
                }
                sendMessageText.setText(""); //set textfield back to empty
            }
        });

        myDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                receiver = dataSnapshot.getValue(ContactUser.class); //user to send message to
                username.setText(receiver.getUsername()); //
                if(receiver.getImageURL().equals("default")){ //set receiver chatbox image
                    profileImage.setImageDrawable(getResources().getDrawable(R.drawable.profileicon));
                }else{
                    Glide.with(MessagingActivity.this).load(receiver.getImageURL()).into(profileImage);
                }
                displayMessages(currentUser.getUid(), userId, receiver.getImageURL()); //current user sends to receiver
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void setSendMessage(String sender, String name, String receiver,  String message){
        myDatabase = FirebaseDatabase.getInstance().getReference("Chats");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("sendername", name);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        myDatabase.push().setValue(hashMap); //create a Chat's database instance and then set values appened to chat database
        //the Chats root database acts like a list, so when I do myDatabase.push(), I'm added new messages to the list
    }
    private void displayMessages(final String myid, final String userId, final String imageurl){ //current user, receiver, image
        myChat = new ArrayList<>();
        myDatabase = FirebaseDatabase.getInstance().getReference("Chats"); //reference Chats database to display all messages
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myChat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class); //chat object to be used for database
                    //this is for the receiver and sender messages display
                    //if sender id equals currentId and receiver equals receiverID
                    if(chat.getSender().equals(myid) && chat.getReceiver().equals(userId) || chat.getSender().equals(userId) && chat.getReceiver().equals(myid)){
                        myChat.add(chat);
                    }
                    chatAdapter = new ChatAdapter(MessagingActivity.this, myChat, imageurl);
                    recyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });
    }
}
