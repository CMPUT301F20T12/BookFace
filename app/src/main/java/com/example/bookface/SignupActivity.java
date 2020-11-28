package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class is used to handle the signup activity for the app
 */
public class SignupActivity extends AppCompatActivity {

    // Initialize constants
    private static final String TAG = "Confirmation" ;

    // Declare variables
    EditText emailID, password, contactField, usernameField;
    Button buttonSignup;
    TextView loginPrompt;
    FirestoreController mFirestoreController;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Assign the values to the variables
        mFirebaseAuth = FirebaseAuth.getInstance();

        emailID = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextNumberPassword);
        contactField = findViewById(R.id.editTextPhone);
        usernameField = findViewById(R.id.username);
        buttonSignup = findViewById(R.id.button);
        loginPrompt = findViewById(R.id.textView);

        // Set the OnCliCkListener for the Signup Button
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailID.getText().toString();
                String pwd = password.getText().toString();
                String contact = contactField.getText().toString();
                String user = usernameField.getText().toString();

                User newUser = new User(user, email, contact);

                if (email.isEmpty() || pwd.isEmpty() || contact.isEmpty() || user.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

                // When email and password are both valid
                else if (!(email.isEmpty() && pwd.isEmpty() && contact.isEmpty() && user.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(
                            SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignupActivity.this, "SignupActivity failed", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                mFirestoreController = new FirestoreController();
                                DocumentReference docRef = mFirestoreController.getDocRef("users", newUser.getUsername());
                                docRef.set(newUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                FirebaseUser userInstance = mFirebaseAuth.getInstance().getCurrentUser();

                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(newUser.getUsername())
                                                        .build();

                                                userInstance.updateProfile(profileUpdates)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    System.out.println("User Updated!"+userInstance.getDisplayName());
                                                                    Toast.makeText(SignupActivity.this, "User Created",
                                                                            Toast.LENGTH_SHORT).show();
                                                                    startActivity(new Intent(SignupActivity.this,
                                                                            MainActivity.class));
                                                                }
                                                            }
                                                        }
                                                );
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SignupActivity.this, "Error in creating User",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(SignupActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set the onClickListener for the loginPrompt
        loginPrompt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
