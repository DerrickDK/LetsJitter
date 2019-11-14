package edu.uga.cs.letsjitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private EditText loginEdit, passwordEdit, firstName, lastName;
    private TextView alreadyHaveAnAccount;
    private Button createAccount;
    private FirebaseAuth authentication;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        authentication = FirebaseAuth.getInstance(); // get authentication instance
        loginEdit = (EditText) findViewById(R.id.loginEmail);
        passwordEdit = (EditText) findViewById(R.id.loginPassword);
        createAccount = (Button) findViewById(R.id.createAccountButton);
        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        alreadyHaveAnAccount = (TextView) findViewById(R.id.haveAnAccountAlready);
        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginScreen = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(loginScreen);
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEdit.getText().toString();
                final String userName = firstName.getText().toString() + " "+lastName.getText().toString();
                String password = passwordEdit.getText().toString();
                if(TextUtils.isEmpty(email)){ //checks to see if email field is empty
                    Toast.makeText(RegisterActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                }if(TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
                } else {
                    //Think about adding a loading bar here
                    authentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) { //create user email and password and store in realtime database
                            if (task.isSuccessful()) {
                                user = authentication.getCurrentUser(); //create a new user
                                String userID = user.getUid(); //gets the new user id
                                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID); //creates a Id for newly created user. The root of this tree is Users
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", userID);
                                hashMap.put("username", userName);
                                hashMap.put("imageURL", "default");

                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent loginScreen = new Intent(RegisterActivity.this, LoginActivity.class);
                                        loginScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(loginScreen); //send user to login screen to sign in
                                        finish();
                                        Toast.makeText(RegisterActivity.this, "Account Creation Successful", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                //if user creates account with same email this message will pop up
                                String message = task.getException().toString();
                                Toast.makeText(RegisterActivity.this, "Oops " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
