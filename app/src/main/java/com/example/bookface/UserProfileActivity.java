package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * This is the class that is used to handle the user Profile activity
 */
public class UserProfileActivity extends AppCompatActivity implements EditProfileFragment.OnFragmentInteractionListener {

    // Initialize constants
    private static final String TAG = "LoginMessage" ;

    // Declare variables
    TextView logoutButton;
    TextView editButton;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser userInstance;
    String userName;
    TextView nameView;
    TextView emailView;
    TextView contactView;

    private BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_confirmation);

        // Assign vales to the variables
        navBar = findViewById(R.id.nav_bar);
        navBar.setOnNavigationItemSelectedListener(navBarMethod);
        mFirebaseAuth = FirebaseAuth.getInstance();
        userInstance = mFirebaseAuth.getCurrentUser();
        if (userInstance != null){
            userName = userInstance.getDisplayName();
            nameView = findViewById(R.id.user_name);
            emailView = findViewById(R.id.user_email);
            contactView = findViewById(R.id.user_contact);
            editButton = findViewById(R.id.edit_profile);

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String docPath = "users/"+userName;
            DocumentReference docRef = db.document(docPath);

            // Retrieve the values from the firebase
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map userData = document.getData();
                            if(userData != null){
                                String username = userData.get("username").toString();
                                String email = userData.get("email").toString();
                                String contact = userData.get("contactNo").toString();

                                nameView.setText(username);
                                emailView.setText(email);
                                contactView.setText(contact);
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        else {
            System.out.println("USERNAME IS NULL!");
        }

        // Set the onClickListener for the logout button
        logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent i = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        // Set the onCLickListener for the Edit Button
        editButton.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {
                new EditProfileFragment(contactView.getText().toString()).show(getSupportFragmentManager(), "Edit_Profile");
            }
        });

    }

    /**
     * This is the method which gets the data of the user and puts it into the edit view
     * @param number
     */
    public void onEditProfileConfirmPressed(String number){
        mFirebaseAuth = FirebaseAuth.getInstance();
        userInstance = mFirebaseAuth.getCurrentUser();
        if (userInstance != null){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String userName = userInstance.getDisplayName();
            String docPath = "users/"+userName;

            DocumentSnapshot x;
            DocumentReference docRef = db.document(docPath);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map userData = document.getData();
                            userData.put("contactNo", number);
                            docRef.set(userData);
                            contactView.setText(number);
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
    }

    /**
     * This is used to implement the bottom navigation bar
     */
    private  BottomNavigationView.OnNavigationItemSelectedListener navBarMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.my_books:
                    Intent toMyBooks = new Intent(UserProfileActivity.this, MyBooks.class);
                    startActivity(toMyBooks);
                    break;
                case R.id.search:
                    Intent toSearch = new Intent(UserProfileActivity.this, SearchActivity.class);
                    startActivity(toSearch);
                    break;
                case R.id.requests:
                    Intent toMyRequests = new Intent(UserProfileActivity.this, MyRequestsActivity.class);
                    startActivity(toMyRequests);
                    break;
            }
            return false;
        }
    };

}