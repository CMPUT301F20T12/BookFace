package com.example.bookface;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * This fragment class is handling the fragment to accept/decline a request
 */
public class RequestAcceptDeclineFragment extends DialogFragment {

    // Variable declarations
    private OnFragmentInteractionListener listener;

    /**
     * Implement the interface methods
     */
    public interface OnFragmentInteractionListener {
        void onDeclineConfirm(DocumentReference reqRef);
    }

    /**
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener){
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // View initializations
        View view = inflater.inflate(R.layout.fragment_request_accept_decline, container, false);
        TextView userName = view.findViewById(R.id.userame_heading);
        TextView userEmail = view.findViewById(R.id.user_email);
        TextView userContact = view.findViewById(R.id.user_contact);
        Button acceptBtn = view.findViewById(R.id.acceptBtn);
        Button declineBtn = view.findViewById(R.id.declineBtn);
        String bookId, requestId;
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Bundle bundle = getArguments();
        if(bundle != null) {
            // Retrieve the passed values through intent
            String requesterName = bundle.getString("REQ_NAME");
            String email = bundle.getString("REQ_EMAIL");
            String contact = bundle.getString("REQ_CONTACT");

//            int position = bundle.getInt("POSITION");

            bookId = bundle.getString("BOOK_ID");
            requestId = bookId.concat(requesterName);

            userName.setText(requesterName);
            userEmail.setText(email);
            userContact.setText(contact);

            DocumentReference bookRef = db.collection("books").document(bookId);
            DocumentReference reqRef = db.collection("requests").document(requestId);

            // On accepting the request, updates the database and directs to SetLocationActvity
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookRef.update("status", "Accepted");
                    reqRef.update("requeststatus", "Accepted").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            // implement notifications here

                            // Updates the book, user and request objects in database
                            bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot book = task.getResult();
                                        if (book.exists()) {
                                            Map bookData = book.getData();

                                            ArrayList<DocumentReference> requestReferences =
                                                    (ArrayList<DocumentReference>) book.get("requestlist");
                                            for (DocumentReference reqRef:requestReferences)
                                            {
                                                reqRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                Map reqData = document.getData();
                                                                String reqStatus = reqData.get("requeststatus").toString();
                                                                DocumentReference borrower =
                                                                        (DocumentReference) reqData.get("borrowerid");
                                                                if(reqStatus.equals("Accepted")==false){
                                                                    borrower.get()
                                                                            .addOnCompleteListener(
                                                                                    new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(
                                                                                @NonNull Task<DocumentSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                DocumentSnapshot document = task.getResult();
                                                                                if (document.exists()) {
                                                                                    Map borrowerData = document.getData();
                                                                                    ArrayList<DocumentReference> sentReq =
                                                                                            (ArrayList<DocumentReference>)
                                                                                                    borrowerData
                                                                                                            .get("sentrequests");
                                                                                    sentReq.remove(reqRef);
                                                                                    borrower.update("sentrequests",
                                                                                            sentReq)
                                                                                            .addOnSuccessListener(new
                                                                                                  OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void aVoid) {
                                                                                            reqRef.delete()
                                                                                                 .addOnSuccessListener(new
                                                                                                   OnSuccessListener<Void>() {
                                                                                                    @Override
                                                                                                    public void onSuccess(
                                                                                                            Void aVoid) {
                                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                                                                                    }
                                                                                                })
                                                                                                .addOnFailureListener(new
                                                                                                  OnFailureListener() {
                                                                                                    @Override
                                                                                                    public void onFailure(
                                                                                                        @NonNull Exception e) {
                                                            Log.w(TAG, "Error deleting document", e);
                                                                                                    }
                                                                                                });
                                                                                        }
                                                                                    })
                                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w(TAG, "Error deleting document", e);
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    Log.d(TAG, "No such document");
                                                                                }
                                                                            } else {
                                                                                Log.d(TAG, "get failed with ",
                                                                                        task.getException());
                                                                            }
                                                                        }
                                                                    });
                                                                }
                                                            } else {
                                                                Log.d(TAG, "No such document");
                                                            }
                                                        } else {
                                                            Log.d(TAG, "get failed with ", task.getException());
                                                        }
                                                    }
                                                });
                                            }



                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                            ArrayList<DocumentReference> updatedReqList = new ArrayList<>();
                            updatedReqList.add(reqRef);
                            bookRef.update("requestlist", updatedReqList);
                            Log.d(TAG, "DocumentSnapshot successfully updated!");

                            Intent intent = new Intent(getActivity(), SetLocationActivity.class);
                            intent.putExtra("REQUEST_ID", requestId);
                            startActivity(intent);
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error updating document", e);
                                }
                            });
                }
            });

            // On decline the request, updates the database and refreshes the view Requests activity
            declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map bookData = document.getData();
                                ArrayList<DocumentReference>bookReqList = (ArrayList<DocumentReference>) bookData
                                        .get("requestlist");
                                bookReqList.remove(reqRef);
                                bookRef.update("requestlist", bookReqList).addOnSuccessListener(new
                                                        OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        reqRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Map reqData = document.getData();
                                                        DocumentReference borrower = (DocumentReference) reqData
                                                                .get("borrowerid");
                                                        borrower.get().addOnCompleteListener(
                                                                new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    DocumentSnapshot document = task.getResult();
                                                                    if (document.exists()) {
                                                                        Map borrowerData = document.getData();
                                                                        ArrayList<DocumentReference> sentReq =
                                                                                (ArrayList<DocumentReference>) borrowerData
                                                                                        .get("sentrequests");
                                                                        sentReq.remove(reqRef);
                                                                        borrower.update("sentrequests",sentReq)
                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                reqRef.delete()
                                                                                        .addOnSuccessListener(
                                                                                                new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                listener.onDeclineConfirm(reqRef);
                                                Log.d(TAG,  "DocumentSnapshot successfully deleted!");
                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(
                                                                                                new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(
                                                                                                    @NonNull Exception e) {
                                                Log.w(TAG, "Error deleting document", e);
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
                                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error updating document", e);
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

        return  view;
    }
}
