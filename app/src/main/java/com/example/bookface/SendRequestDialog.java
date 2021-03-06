package com.example.bookface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Handles the accept/decline of the request
 */
public class SendRequestDialog extends AppCompatDialogFragment {

    FirebaseAuth mFirebaseAuth;
    private OnFragmentInteractionListener listener;

    /**
     * Implement the interface method
     */
    public interface OnFragmentInteractionListener {
        void onSendRequestConfirm();
    }

    /**
     * Override the onAttach method
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SendRequestDialog.OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * This creates the dialog box to send the request
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Send Request?")
                .setMessage("This will send a book request to the owner. You will be notified if your request is accepted.")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Grab the bookid and userid
                        Bundle bundle = getArguments();
                        String bookid = bundle.getString("bookid");
                        String borrowerid = bundle.getString("borrowerid");

                        DocumentReference reqBookRef = db.collection("books").document(bookid);
                        DocumentReference borrowerRef = db.collection("users").document(borrowerid);

                        String requestid = bookid+borrowerid;

                        // Add a request to requests collection in Firestore
                        Map<String, Object> request = new HashMap<>();
                        request.put("bookid", reqBookRef);
                        request.put("borrowerid", borrowerRef);
                        request.put("requeststatus", "pending");
                        request.put("exchangeowner", 0);
                        request.put("exchangeborrower", 0);

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


                        DocumentReference requestRef = db.collection("requests").document(requestid);

                        // add request docref to sentrequests for the borrower
                        borrowerRef.update("sentrequests", FieldValue.arrayUnion(requestRef))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    reqBookRef.update("requestlist", FieldValue.arrayUnion(requestRef))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            listener.onSendRequestConfirm();
                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error updating document", e);
                                                }
                                            });
                                }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });
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