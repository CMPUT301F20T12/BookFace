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

public class UserProfileActivity extends AppCompatActivity implements EditProfileFragment.OnFragmentInteractionListener{

    private static final String TAG = "LoginMessage" ;
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

        navBar = findViewById(R.id.nav_bar);

        navBar.setOnNavigationItemSelectedListener(navBarMethod);
//        System.out.println("Query started");
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
//            System.out.println(docPath);
            DocumentReference docRef = db.document(docPath);

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
        else{
            System.out.println("USERNAME IS NULL!");
        }

        logoutButton = findViewById(R.id.logout);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent i = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(i);
            }
        });



        editButton.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {
                new EditProfileFragment(contactView.getText().toString()).show(getSupportFragmentManager(), "Edit_Profile");
            }
        });

    }

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


    private  BottomNavigationView.OnNavigationItemSelectedListener navBarMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


            switch (menuItem.getItemId()){
                case R.id.my_books:
                    Intent toMyBooks = new Intent(UserProfileActivity.this, MyBooks.class);
                    startActivity(toMyBooks);
                    break;
//                case R.id.requests:
//                    Intent toRequests = new Intent(LoginConfirmationActivity.this, SignupActivity.class);
//                    startActivity(toRequests);
//                    break;
//                case R.id.search:
//                    Intent toSearch = new Intent(LoginConfirmationActivity.this, SignupActivity.class);
//                    startActivity(toSearch);
//                    break;
//                case R.id.notification:
//                    Intent toNotification = new Intent(LoginConfirmationActivity.this, SignupActivity.class);
//                    startActivity(toNotification);
//                    break;
            }
            return false;
        }
    };

}