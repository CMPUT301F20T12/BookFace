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

/**
 * Displays the list of requests on a book
 */
public class ViewRequestActivity extends AppCompatActivity implements RequestAcceptDeclineFragment.OnFragmentInteractionListener {

    ListView requestListView;
    ArrayAdapter<DocumentReference> requestListAdapter;
    ArrayList<DocumentReference> requestDocReferences;

    String bookId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_request);

        requestListView = findViewById(R.id.requestList);
        bookId = this.getIntent().getStringExtra("BOOK_ID");

        DocumentReference docRef = db.collection("books").document(bookId);

        // Extracts a list of requests on a book and sets adapter
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "ViewRequestActivity_MSG";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        requestDocReferences = (ArrayList<DocumentReference>) document.get("requestlist");
                        if(requestDocReferences!=null){

                            requestListView = findViewById(R.id.requestList);

                            requestListAdapter = new ViewRequestUserList(ViewRequestActivity.this, requestDocReferences);
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
                                                    DocumentReference requesterRef = (DocumentReference)
                                                            requestData.get("borrowerid");
                                                    requesterRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                DocumentSnapshot requester = task.getResult();
                                                                if (requester.exists()) {
                                                                    Map requesterData = requester.getData();
                                                                    String requesterName =
                                                                            requesterData.get("username").toString();
                                                                    String requesterEmail =
                                                                            requesterData.get("email").toString();
                                                                    String requesterContact =
                                                                            requesterData.get("contactNo").toString();

                                                                    Bundle bundle = new Bundle();
                                                                    bundle.putInt("POSITION",position);
                                                                    bundle.putString("BOOK_ID",bookId);
                                                                    bundle.putString("REQ_NAME", requesterName);
                                                                    bundle.putString("REQ_EMAIL", requesterEmail);
                                                                    bundle.putString("REQ_CONTACT", requesterContact);

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

    /**
     * Implements the interface to refresh view Requests activity
     * @param reqRef
     */
    public void onDeclineConfirm(DocumentReference reqRef) {
        requestDocReferences.remove(reqRef);
        requestListAdapter.notifyDataSetChanged();
        finish();
    }
}
