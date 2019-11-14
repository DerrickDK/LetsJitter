package edu.uga.cs.letsjitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingActivity extends AppCompatActivity {
    private CircleImageView profileImage;
    private TextView username, sendMessageText;
    private Button sendButton;
    private FirebaseUser user;
    private FirebaseAuth authentication;
    private DatabaseReference myDatabase;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        profileImage = (CircleImageView) findViewById(R.id.profileimage);
        username = (TextView) findViewById(R.id.userName);
        sendMessageText = (TextView) findViewById(R.id.messageEditText);
        sendButton = (Button) findViewById(R.id.sendButton);
        authentication = FirebaseAuth.getInstance();
        user = authentication.getCurrentUser();
        intent = getIntent();
        final String userId = intent.getStringExtra("userID");
        System.out.println("Intent ID: "+userId);

//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String message = sendMessageText.getText().toString();
//                if(message != null || !message.equals("")){
//                    setSendMessage(user.getUid(), userId, message); //current user(user.getUid() sending to other user (userId)
//                }else{
//                    Toast.makeText(MessagingActivity.this, "Can't send an empty message", Toast.LENGTH_SHORT).show();
//                }
//                sendMessageText.setText(""); //set back to empty
//            }
//        });
        myDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        myDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ContactUser user = dataSnapshot.getValue(ContactUser.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profileImage.setImageDrawable(getResources().getDrawable(R.drawable.profileicon));
                }else {
                    Glide.with(MessagingActivity.this).load(user.getImageURL()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
//    private void setSendMessage(String sender, String receiver, String message){
//        myDatabase = FirebaseDatabase.getInstance().getReference();
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("sender", sender);
//        hashMap.put("receiver", receiver);
//        hashMap.put("message", message);
//        myDatabase.child("Chats").push().setValue(hashMap);
//    }
}
