package com.example.bookface;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * This is a class that keeps track of a list of request objects
 */
public class RequestList extends ArrayAdapter<DocumentReference> {

    private ArrayList<DocumentReference> myRequests;
    private Context context;

    public RequestList(Context context, ArrayList<DocumentReference> myRequests) {
        super(context, 0, myRequests);
        this.myRequests = myRequests;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        System.out.println("IN REQUEST LIST");

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.request, parent, false);
        }

        View finalView = view;

        DocumentReference requestRef = myRequests.get(position);
        TextView requestStatus = finalView.findViewById(R.id.request_status);
        TextView requestBookTitle = finalView.findViewById(R.id.request_book_title);
        TextView requestBookAuthor = finalView.findViewById(R.id.request_book_author);
        System.out.println("REQUEST: "+requestRef);

        requestRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot request = task.getResult();
                    System.out.println("REQ: "+request);
                    if (request.exists()) {

                        Map requestData = request.getData();

                        DocumentReference bookRef = (DocumentReference) requestData.get("bookid");
                        System.out.println("Book REF: "+bookRef);

                        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map bookData = document.getData();
                                        String title = bookData.get("title").toString();
                                        String author = bookData.get("author").toString();
                                        String status = requestData.get("requeststatus").toString();
//
                                        requestStatus.setText(status);
                                        requestBookTitle.setText(title);
                                        requestBookAuthor.setText(author);

                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });


//                        final Book requestedBook = request.getBookRequested();
//                        requestBookTitle.setText();
//                        requestBookAuthor.setText();
//                        requestStatus.setText();
//
//                        requesterView.setText(requesterName);

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return view;
    }

    public int getCount() {
        return myRequests.size();
    }
}

