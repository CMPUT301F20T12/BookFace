package com.example.bookface;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.logging.Level;

public class FirestoreController {
  private FirebaseFirestore db;

  public FirestoreController() {
    this.db = FirebaseFirestore.getInstance();
  }

  public DocumentReference getDocRef(String collectionPath, String documentPath) {
    DocumentReference docRef = this.db.collection(collectionPath).document(documentPath);
    return docRef;
  }

  public CollectionReference getCollRef(String collectionPath) {
    CollectionReference collRef = this.db.collection(collectionPath);
    return collRef;
  }

  public FirebaseFirestore getDb() {
    return db;
  }
}
