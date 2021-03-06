package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

        requestListView = findViewById(R.id.requestList);

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
                        requestListAdapter = new RequestList(context, requests);
                        requestListView.setAdapter(requestListAdapter);

                        navBar = findViewById(R.id.nav_bar);

                        navBar.setOnNavigationItemSelectedListener(navBarMethod);
                    }
                }
            }
        });

        // sets onItemClickListener and intents according to the status
        requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                String reqRef = requests.get(position).getId();
                String docPathReq = "requests/"+reqRef;
                DocumentReference docRefReq = db.document(docPathReq);
                docRefReq.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // set the books in the recyclerView
                                String status = document.get("requeststatus").toString();
                                DocumentReference bookid = (DocumentReference) document.get("bookid");
                                if(status.toLowerCase().equals("pending")){
                                    Intent intent = new Intent(MyRequestsActivity.this, BookDescription.class);
                                    intent.putExtra("MyRequestActivity", bookid.getId());
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(MyRequestsActivity.this,
                                            BookExchangeDisplayActivity.class);
                                    intent.putExtra("REQUEST_ID", reqRef);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                });
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
                case R.id.profile:
                    Intent toMyProfile = new Intent(MyRequestsActivity.this, UserProfileActivity.class);
                    startActivity(toMyProfile);
                    break;
                case R.id.search:
                    Intent toRequests = new Intent(MyRequestsActivity.this, SearchActivity.class);
                    startActivity(toRequests);
                    break;
            }
            return false;
        }
    };

}
