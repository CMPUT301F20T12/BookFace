package com.example.bookface;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class ViewRequestActivity extends AppCompatActivity {

    ListView requestListView;
    ArrayAdapter<DocumentReference> requestListAdapter;

//    ArrayList<Request> requestDataList;
    String bookIsbn;
    String bookOwner;
    String bookId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_request);

        requestListView = findViewById(R.id.requestList);
        bookIsbn = this.getIntent().getStringExtra("BOOK_ISBN");
        bookOwner = this.getIntent().getStringExtra("BOOK_OWNER");
        bookId = bookIsbn.concat(bookOwner);

        System.out.println("ISBN IN VIEW_ACTIVITY  "+ bookId);

        DocumentReference docRef = db.collection("books").document(bookId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "ViewRequestActivity_MSG";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        ArrayList<DocumentReference> requestDocReferences = (ArrayList<DocumentReference>) document.get("requestList");
                        if(requestDocReferences!=null){

                            requestListView = findViewById(R.id.requestList);

                            requestListAdapter = new ViewRequestUserList(ViewRequestActivity.this, requestDocReferences);
                            requestListView.setAdapter(requestListAdapter);
                        }

                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

//        requestDataList = (ArrayList<Request>) b.get("REQ_LIST");
//        requestListAdapter = new ViewRequestUserList(this,requestDataList);
//        requestListView.setAdapter(requestListAdapter);
    }
}
