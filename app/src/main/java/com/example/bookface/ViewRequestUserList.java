package com.example.bookface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.journeyapps.barcodescanner.DecoderResultPointCallback;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nullable;

import static android.content.ContentValues.TAG;

/**
 * Displays the username of the requester of the book
 */
public class ViewRequestUserList extends ArrayAdapter<DocumentReference>{

    // variable declarations
    private ArrayList<DocumentReference> requestList;
    private Context context;

    /**
     * This is the contructor
     * @param context
     * @param requestList
     */
    public ViewRequestUserList(Context context, ArrayList<DocumentReference> requestList){
        super(context,0, requestList);
        this.requestList = requestList;
        this.context = context;
    }

    /**
     * Displays the username of the requester of the book
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.view_request_user, parent,false);
        }

        final DocumentReference requestDocReference = requestList.get(position);
        View finalView = view;
        requestDocReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot request = task.getResult();
                    if (request.exists()) {
                        TextView requesterView = finalView.findViewById(R.id.requesterName);
                        Map requestData = request.getData();
                        DocumentReference borrowerRef = (DocumentReference) requestData.get("borrowerid");

                        borrowerRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map borrowerData = document.getData();
                                        String requesterName = borrowerData.get("username").toString();
                                        requesterView.setText(requesterName);
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
        return view;
    }

    /**
     * This returns the number of requests
     * @return
     * the number of requests
     */
    public int getCount() {
        if(requestList != null) {
            return requestList.size();
        }
        else {
            return 0;
        }
    }

}

