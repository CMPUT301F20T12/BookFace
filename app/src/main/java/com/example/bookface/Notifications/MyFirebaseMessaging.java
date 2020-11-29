package com.example.bookface.Notifications;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessaging extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage mRemoteMessage) {
        super.onMessageReceived(mRemoteMessage);

        String ReceiverID = mRemoteMessage.getData().get("ReceiverUserId");

        SharedPreferences sharedPref = getSharedPreferences("PREFS", MODE_PRIVATE);

        String currentOnlineUser = sharedPref.getString("currentUser", "none");

        // TODO: Maybe need to get email instead of the FirebaseUser?
        // TODO: Sending message needs to give the UID of firebase user
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null && ReceiverID.equals(firebaseUser.getUid())) {
            if (currentOnlineUser != null){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    sendOreoNotification(mRemoteMessage);
                }

                else {
                    Log.d(TAG, "MINIMUM SDK REQ. NOT MET:");
                }
            }
        }
    }

    private void sendOreoNotification(RemoteMessage mRemoteMessage){
        String notificationTitle = mRemoteMessage.getData().get("Title");
        String notificationBody = mRemoteMessage.getData().get("Message");


    }
}
