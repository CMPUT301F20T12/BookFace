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

import com.example.bookface.Notifications.APIService;
import com.example.bookface.Notifications.Client;
import com.example.bookface.Notifications.Data;
import com.example.bookface.Notifications.MyResponse;
import com.example.bookface.Notifications.Sender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.ContentValues.TAG;

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

    public void sendNotification(String OwnerId){
        String message = "You have a new request!";

        Data notificationData = new Data("BookFace", message, OwnerId);

        // Get token of the OwnerId

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference OwnerUser = db.collection("users").document(OwnerId);

        OwnerUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        String token = document.getData().get("token").toString();

                        Sender sender = new Sender(notificationData, token);

                        APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

                        apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if (response.code() == 200)
                                {
                                    if (response.body().Success != 1)
                                    {
                                        Log.d(TAG, "Notification send failed");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Log.d(TAG, "notificationApi send notification on failure");
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

                        // Get the owner id

                        reqBookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                                        String ownerId = document.getData().get("ownerUsername").toString();


                                        // TODO: Send notification here
                                        sendNotification(ownerId);
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });


                        String requestid = bookid+borrowerid;

                        // Add a request to requests collection in Firestore
                        Map<String, Object> request = new HashMap<>();
                        request.put("bookid", reqBookRef);
                        request.put("borrowerid", borrowerRef);
                        request.put("requeststatus", "pending");
                        request.put("exchangeowner", 0);
                        request.put("exchangeborrower", 0);

                        System.out.println("REQ OBJ: "+request);

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
                        borrowerRef.update("sentrequests", FieldValue.arrayUnion(requestRef)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                reqBookRef.update("requestlist", FieldValue.arrayUnion(requestRef)).addOnSuccessListener(new OnSuccessListener<Void>() {
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



//                        // add request docref to requestlist for the book
//                        reqBookRef.update("requestlist", FieldValue.arrayUnion(requestRef));
//
//                        // redirect to MyRequests
//                        Intent toMyRequests = new Intent(SendRequestDialog.this.getActivity(), MyRequestsActivity.class);
//                        startActivity(toMyRequests);
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

//package com.example.bookface;
//
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatDialogFragment;
//
//public class SendRequestDialog extends AppCompatDialogFragment {
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Send Request?")
//                .setMessage("This will send a book request. The owner will be notified if your request is accepted")
//                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // Send Request
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }
//                });
//
//        return builder.create();
//    }
//}
