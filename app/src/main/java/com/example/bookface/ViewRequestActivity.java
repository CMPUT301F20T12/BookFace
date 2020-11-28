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
    String bookId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_request);

        requestListView = findViewById(R.id.requestList);
        bookId = this.getIntent().getStringExtra("BOOK_ID");

        System.out.println("ISBN IN VIEW_ACTIVITY  "+ bookId);

        DocumentReference docRef = db.collection("books").document(bookId);
        System.out.println("DOC REF "+docRef);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "ViewRequestActivity_MSG";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    System.out.println("DOCUMENT "+document);
                    if (document.exists()) {
                        ArrayList<DocumentReference> requestDocReferences = (ArrayList<DocumentReference>) document.get("requestlist");
                        System.out.println("REQ DOC REF IN VIEW "+requestDocReferences);
                        if(requestDocReferences!=null){

                            requestListView = findViewById(R.id.requestList);

                            requestListAdapter = new ViewRequestUserList(ViewRequestActivity.this, requestDocReferences);
                            System.out.println("DOC REFERENCES "+requestDocReferences);
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
