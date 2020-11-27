package com.example.bookface;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ViewRequestActivity extends AppCompatActivity {

    ListView requestListView;
    ArrayAdapter<Request> requestListAdapter;
    ArrayList<Request> requestDataList;
    FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_request);

        requestListView = findViewById(R.id.requestList);

//        requestDataList = new ArrayList<>();
        requestDataList = (ArrayList<Request>) getIntent().getExtras().get("REQ_LIST");
        requestListAdapter = new ViewRequestUserList(this, requestDataList);
        requestListView.setAdapter(requestListAdapter);
    }
}
