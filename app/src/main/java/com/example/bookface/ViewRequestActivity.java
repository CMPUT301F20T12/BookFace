package com.example.bookface;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Map;

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


                            requestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    DocumentReference requestRef = requestDocReferences.get(position);
                                    requestRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {

                                                    Map requestData = document.getData();
                                                    DocumentReference requesterRef = (DocumentReference) requestData.get("borrowerid");
                                                    System.out.println(requesterRef);
                                                    requesterRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot requester = task.getResult();
                                                                if (requester.exists()) {
                                                                    Map requesterData = requester.getData();
                                                                    String requesterName = requesterData.get("username").toString();
                                                                    String requesterEmail = requesterData.get("email").toString();
                                                                    String requesterContact = requesterData.get("contactNo").toString();
//
                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putString("BOOK_ID",bookId);
                                                                    bundle.putString("REQ_NAME", requesterName);
                                                                    bundle.putString("REQ_EMAIL", requesterEmail);
                                                                    bundle.putString("REQ_CONTACT", requesterContact);
//
                                                                    System.out.println(bundle);

                                                                    RequestAcceptDeclineFragment requestAcceptDeclineFragment = new RequestAcceptDeclineFragment();
                                                                    requestAcceptDeclineFragment.setArguments(bundle);
                                                                    requestAcceptDeclineFragment.show(getSupportFragmentManager(),"userProfileFragment");

                                                                } else {
                                                                    Log.d(TAG, "No such document");
                                                                }
                                                            } else {
                                                                Log.d(TAG, "get failed with ", task.getException());
                                                            }
                                                        }
                                                    });

                                                } else {
                                                    Log.d(TAG, "No such document");
                                                }
                                            } else {
                                                Log.d(TAG, "get failed with ", task.getException());
                                            }
                                        }
                                    });

                                }
                            });

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

    }
}
