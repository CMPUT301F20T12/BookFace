package com.example.bookface;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class BookList extends ArrayAdapter<String> {

    private ArrayList<String> myBooks;
    private Context context;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser userInstance;

    public BookList(Context context, ArrayList<String> myBooks){
        super(context,0, myBooks);
        this.myBooks = myBooks;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;


        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.book, parent,false);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        userInstance = mFirebaseAuth.getCurrentUser();
        if (userInstance != null){
            String userName = userInstance.getDisplayName();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String docPath = "users/"+userName;
            DocumentReference docRef = db.document(docPath);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                private static final String TAG = "BookListMessage";

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
//                            Map userData = document.getData();
//                            DocumentSnapshot document = task.getResult();
//                            if(userData != null){
                            List<String> booksOwned = (List<String>) document.get("booksOwned");
                            String bookISBN = booksOwned.get(position);
//                            TextView isbn = findViewById();
//                            isbn.setText(bookISBN);
                            Log.d(TAG, "onComplete: "+bookISBN);

//                                TextView nameView = findViewById(R.id.user_name);
//                                TextView emailView = findViewById(R.id.user_email);
//                                TextView contactView = findViewById(R.id.user_contact);
//
//                                nameView.setText(username);
//                                emailView.setText(email);
//                                contactView.setText(contact);

                            }
                        else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }

        return view;
    }
}
