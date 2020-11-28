package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This activity is used to show the requests that the user has sent
 */
public class MyRequestsActivity extends AppCompatActivity {
    ListView requestListView;
    ArrayAdapter<DocumentReference> requestListAdapter;
    ArrayList<DocumentReference> requests;
    FirebaseFirestore db;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser userInstance;

    private BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);


//        requests = new ArrayList<>();

        requestListView = findViewById(R.id.requestList);
//        requestListAdapter = new RequestList(this, requests);
//        requestListView.setAdapter(requestListAdapter);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userInstance = mFirebaseAuth.getCurrentUser();

        // Find the user document from the firebase
        String userName = userInstance.getDisplayName();
        db = FirebaseFirestore.getInstance();
        String docPath = "users/"+userName;
        DocumentReference docRef = db.document(docPath);

        Context context = this;

        // Read the user document to retrieve all the requests they have sent out
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // set the books in the recyclerView
                        requests = (ArrayList<DocumentReference>) document.get("sentrequests");
                        System.out.println("THIS IS REQUEST SENT: "+requests);
                        requestListAdapter = new RequestList(context, requests);
                        requestListView.setAdapter(requestListAdapter);

                        navBar = findViewById(R.id.nav_bar);

                        navBar.setOnNavigationItemSelectedListener(navBarMethod);
                    }
                }
            }
        });
    }

    /**
     * This is used to implement the bottom navigation bar
     */
    private  BottomNavigationView.OnNavigationItemSelectedListener navBarMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){
                case R.id.my_books:
                    Intent toMyBooks = new Intent(MyRequestsActivity.this, MyBooks.class);
                    startActivity(toMyBooks);
                    break;
                case R.id.requests:
                    Intent toRequests = new Intent(MyRequestsActivity.this, MyRequestsActivity.class);
                    startActivity(toRequests);
                    break;
                case R.id.profile:
                    Intent toMyProfile = new Intent(MyRequestsActivity.this, UserProfileActivity.class);
                    startActivity(toMyProfile);
                    break;
//                case R.id.notification:
//                    Intent toNotification = new Intent(LoginConfirmationActivity.this, SignupActivity.class);
//                    startActivity(toNotification);
//                   s break;
            }
            return false;
        }
    };

}




//package com.example.bookface;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.util.ArrayList;
//
///**
// * This activity is used to show the requests that the user has sent
// */
//public class MyRequestsActivity extends AppCompatActivity {
//    ListView requestListView;
//    ArrayAdapter<Request> requestListAdapter;
//    ArrayList<Request> requests;
//    FirebaseFirestore db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_requests);
//
//        requestListView = findViewById(R.id.requestList);
//        requests = new ArrayList<>();
//
//        requestListAdapter = new RequestList(this, requests);
//        requestListView.setAdapter(requestListAdapter);
//
//        db = FirebaseFirestore.getInstance();
//    }
//}
