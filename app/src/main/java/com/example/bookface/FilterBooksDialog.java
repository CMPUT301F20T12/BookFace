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
 * Displays a list of statuses to filter by
 */
public class FilterBooksDialog extends AppCompatDialogFragment {

    FirebaseAuth mFirebaseAuth;
    private OnFragmentInteractionListener listener;

    /**
     * Implement the interface method
     */
    public interface OnFragmentInteractionListener {
        void onStatusSelected(String status);
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
     * This creates the dialog box to choose the status you want to filter by
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] statuses = new String[]{"All", "Available", "Requested", "Accepted", "Borrowed"};

        builder.setTitle("Show me")
                .setItems(statuses, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                listener.onStatusSelected("All");
                                break;
                            case 1:
                                listener.onStatusSelected("Available");
                                break;
                            case 2:
                                listener.onStatusSelected("Requested");
                                break;
                            case 3:
                                listener.onStatusSelected("Accepted");
                                break;
                            case 4:
                                listener.onStatusSelected("Borrowed");
                                break;
                        }
                    }
                });

        return builder.create();
    }
}