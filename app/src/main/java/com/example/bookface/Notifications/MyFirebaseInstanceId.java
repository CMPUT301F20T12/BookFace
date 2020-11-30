package com.example.bookface.Notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceId extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("myFirebaseId", "Token is: " + refreshToken);
        if (firebaseUser != null){
            updateToken(refreshToken);
        }

    }



    private void updateToken(String refreshToken){
        Log.d("tokenchecker", "Token getting updated");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String username = firebaseUser.getDisplayName();
        System.out.println("USER FOR TOKEN GEN: "+username);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(username);
        final String TAG = "TOKEN_MSG";
        userRef
                .update("token", refreshToken)
                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Token Set in user");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });

        Token token = new Token(refreshToken);


    }
}
