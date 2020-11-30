package com.example.bookface.Notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;
import java.util.Map;

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
        System.out.println(username);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Token token = new Token(refreshToken);


    }
}
