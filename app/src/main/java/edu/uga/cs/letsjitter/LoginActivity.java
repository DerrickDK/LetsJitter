package edu.uga.cs.letsjitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
   // private FirebaseUser user;
    private FirebaseAuth authentication;
    private EditText loginEdit, passwordEdit;
    private TextView needNewAccount, forgotPassword;
    private Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authentication = FirebaseAuth.getInstance(); // get authentication instance
        //user = authentication.getCurrentUser();
        loginEdit = (EditText) findViewById(R.id.loginEmail);
        passwordEdit = (EditText) findViewById(R.id.loginPassword);
        needNewAccount = (TextView) findViewById(R.id.needNewAccount);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        needNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerScreen = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerScreen);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                if(TextUtils.isEmpty(email)){ //checks to see if email field is empty
                    Toast.makeText(LoginActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                }if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Enter your password", Toast.LENGTH_SHORT).show();
                }else {
                    //think about adding a loading bar here
                    authentication.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() { //this method will know if I have a current user or not
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //send user to the main activity intent
                                Intent mainScreen = new Intent(LoginActivity.this, MainActivity.class);
                                mainScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainScreen);
                                finish(); //when use is signed in we don't want them to click the back button to go back to the login screen, rather they need to logout and they will go to the main activity screen
                               // Toast.makeText(LoginActivity.this, "Successful login", Toast.LENGTH_SHORT).show();
                            } else {
                                String message = task.getException().toString();
                                Toast.makeText(LoginActivity.this, "Oops " + message, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
    }



//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(user != null){ // if user is logged in, send user to main activity to view contacts, chats, group
//            Intent mainScreen = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(mainScreen);
//        }
//    }
}
