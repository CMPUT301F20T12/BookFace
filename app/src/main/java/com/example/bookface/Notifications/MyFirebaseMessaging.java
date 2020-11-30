package com.example.bookface.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.bookface.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendOreoNotification(RemoteMessage mRemoteMessage){
        String notificationTitle = mRemoteMessage.getData().get("Title");
        String notificationBody = mRemoteMessage.getData().get("Message");

        Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setContentTitle(notificationTitle)
                .setContentText(notificationBody)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // for lower than api 26
                .setContentIntent(pendingIntent)
                .setAutoCancel(true) // removes notification on click
                .build();

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(new Random().nextInt(), notification);
    }
}
