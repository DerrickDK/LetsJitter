package edu.uga.cs.letsjitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagingActivity extends AppCompatActivity {
    private CircleImageView profileImage;
    private TextView username;
    private FirebaseUser user;
    private FirebaseAuth authentication;
    private DatabaseReference myDatabase;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        profileImage = (CircleImageView) findViewById(R.id.profileImage);
        username = (TextView) findViewById(R.id.username);
        authentication = FirebaseAuth.getInstance();
        intent = getIntent();
        String userId = intent.getStringExtra("userID");
        System.out.println("Intent ID: "+userId);
        user = authentication.getCurrentUser();
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
}
