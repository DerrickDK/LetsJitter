package edu.uga.cs.letsjitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPages;
    private TabLayout tabLayout;
    private TabAdapter tabAdapter;
    private FirebaseUser user;
    private FirebaseAuth authentication;
    private DatabaseReference myDatabase;
    private EditText groupField;

    /**
     * This is the main method that gets the current user information and verify's them before login
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPages = (ViewPager) findViewById(R.id.main_tabs_pager);
        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPages.setAdapter(tabAdapter);
        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPages);
    }

    @Override
    protected void onStart() {
        super.onStart();
        authentication = FirebaseAuth.getInstance();
        myDatabase = FirebaseDatabase.getInstance().getReference("Users");
        user = authentication.getCurrentUser(); //checks our database and gets the user information
        if(user == null){ //if user has not logged in, send user to login screen before displaying contacts, chat, and groups
            Intent loginScreen = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginScreen);
        }else { //if user is already logged in we want to verify before they start
            myDatabase.child(user.getUid()).addValueEventListener(new ValueEventListener() { //looking into the database for current user information
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //for email verification
                    if((dataSnapshot.hasChild("username") && dataSnapshot.hasChild("id"))){ //if the user has a name and the id exist
                        String userName = dataSnapshot.child("username").getValue().toString(); //save username
                        Toast.makeText(MainActivity.this, "Welcome "+userName, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "Error ", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.menuGroup){ //opens up a dialog where user can create a group
            AlertDialog.Builder dialogBox = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialog);
            dialogBox.setTitle("Create your Group Name: ");
            groupField = new EditText(MainActivity.this);
            groupField.setHint("Enter group name:");
            dialogBox.setView(groupField); //create the view
            dialogBox.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    final String getGroupName = groupField.getText().toString();
                    if(TextUtils.isEmpty(getGroupName)){
                        Toast.makeText(MainActivity.this, "Please enter a group name or cancel", Toast.LENGTH_SHORT).show();
                    }else {
                        myDatabase = FirebaseDatabase.getInstance().getReference("Groups"); //root
                        myDatabase.child(getGroupName).setValue("").addOnCompleteListener(new OnCompleteListener<Void>() { //add a child to the root Group reference
                            @Override
                            public void onComplete(Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "Created: "+getGroupName, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }

            });
            dialogBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { //when user wants to cancel
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            dialogBox.show();
        }
        return true;
    }
}
