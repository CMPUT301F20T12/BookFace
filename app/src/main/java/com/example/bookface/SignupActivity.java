package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    EditText emailID, password, contactField, usernameField;
    Button buttonSignup;
    TextView loginPrompt;

    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mFirebaseAuth = FirebaseAuth.getInstance();

        emailID = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextNumberPassword);
        contactField = findViewById(R.id.editTextPhone);
        usernameField = findViewById(R.id.username);
        buttonSignup = findViewById(R.id.button);
        loginPrompt = findViewById(R.id.textView);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String pwd = password.getText().toString();
                String contact = contactField.getText().toString();
                String user = usernameField.getText().toString();

                if (email.isEmpty() || pwd.isEmpty() || contact.isEmpty() || user.isEmpty()){
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

                // When email and password are both valid
                else if (!(email.isEmpty() && pwd.isEmpty() && contact.isEmpty() && user.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignupActivity.this, "SignupActivity failed", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            }
                        }
                    });
                }

                else {
                    Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}