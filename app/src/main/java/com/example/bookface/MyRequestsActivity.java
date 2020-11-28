package com.example.bookface;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This activity is used to show the requests that the user has sent
 */
public class MyRequestsActivity extends AppCompatActivity {
    ListView requestListView;
    ArrayAdapter<Request> requestListAdapter;
    ArrayList<Request> requests;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        requestListView = findViewById(R.id.requestList);
        requests = new ArrayList<>();

        requestListAdapter = new RequestList(this, requests);
        requestListView.setAdapter(requestListAdapter);

        db = FirebaseFirestore.getInstance();
    }
}
