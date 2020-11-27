package com.example.bookface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SendRequestDialog extends AppCompatDialogFragment {

    FirebaseAuth mFirebaseAuth;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Send Request?")
                .setMessage("This will send a book request. The owner will be notified if your request is accepted")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Grab the bookid and userid
                        Bundle bundle = getArguments();
                        String bookid = bundle.getString("bookid");
                        String borrowerid = bundle.getString("borrowerid");

                        String requestid = bookid+borrowerid;

                        // Add a request to requests collection in Firestore
                        Map<String, Object> request = new HashMap<>();
                        request.put("bookid", bookid);
                        request.put("borrowerid", borrowerid);
                        request.put("requeststatus", "pending");

                        db.collection("requests").document(requestid)
                                .set(request)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error writing document", e);
                                    }
                                });

                        DocumentReference borrowerRef = db.collection("users").document(borrowerid);
                        DocumentReference requestRef = db.collection("requests").document(requestid);
                        DocumentReference bookRequestedRef = db.collection("books").document(bookid);

                        // add request docref to sentrequests for the borrower
                        borrowerRef.update("sentrequests", FieldValue.arrayUnion(requestid));

                        // add request docref to requestlist for the book
                        bookRequestedRef.update("requestlist", FieldValue.arrayUnion(requestRef));

                        // redirect to MyRequests
                        Intent toMyRequests = new Intent(SendRequestDialog.this.getActivity(), MyRequestsActivity.class);
                        startActivity(toMyRequests);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });

        return builder.create();
    }
}