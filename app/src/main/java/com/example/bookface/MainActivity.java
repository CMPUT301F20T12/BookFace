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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is the main activity that is run
 */
public class MainActivity extends AppCompatActivity {

    // Declare variables
    EditText emailID, password;
    Button buttonLogin;
    TextView SignupPrompt;
    FirebaseAuth mFirebaseAuth;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    /**
     * onCreate method is overwritten
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // View initializations
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextNumberPassword);
        buttonLogin = findViewById(R.id.button);
        SignupPrompt = findViewById(R.id.textView);

        // Firebase listener
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, UserProfileActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this, "Not Logged In", Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Login button logic
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailID.getText().toString();
                String pwd = password.getText().toString();

                // Validate the inputs
                if (email.isEmpty() || pwd.isEmpty()){

                    if(email.isEmpty()){
                        emailID.setError("Enter email");
                        emailID.requestFocus();
                    }
                    if (pwd.isEmpty()){
                        password.setError("Enter password");
                        password.requestFocus();
                    }

                    if (email.isEmpty() && pwd.isEmpty()){
                        Toast.makeText(MainActivity.this, "Both fields empty", Toast.LENGTH_SHORT).show();
                    }

                }

                // When email and password are both valid
                else if (!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // Go to User Profile
                                Intent i = new Intent(MainActivity.this, UserProfileActivity.class);
                                startActivity(i);
                            }
                        }
                    });
                }
                // Login error
                else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });

        SignupPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toSignup = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(toSignup);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
