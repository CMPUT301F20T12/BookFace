package com.example.bookface;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * This is a class that keeps track of a list of request objects
 */
public class RequestList extends ArrayAdapter<DocumentReference> {

    // Declare variables
    private ArrayList<DocumentReference> myRequests;
    private Context context;

    /**
     * This is the constructor
     * @param context
     * @param myRequests
     */
    public RequestList(Context context, ArrayList<DocumentReference> myRequests) {
        super(context, 0, myRequests);
        this.myRequests = myRequests;
        this.context = context;
    }

    /**
     * This is the custom Array adapter
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.request, parent, false);
        }

        View finalView = view;

        DocumentReference requestRef = myRequests.get(position);
        TextView requestStatus = finalView.findViewById(R.id.request_status);
        TextView requestBookTitle = finalView.findViewById(R.id.request_book_title);
        TextView requestBookAuthor = finalView.findViewById(R.id.request_book_author);

        // Sets the text view fields in the Request list activity
        requestRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot request = task.getResult();
                    if (request.exists()) {

                        Map requestData = request.getData();

                        DocumentReference bookRef = (DocumentReference) requestData.get("bookid");

                        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map bookData = document.getData();
                                        String title = bookData.get("title").toString();
                                        String author = bookData.get("author").toString();
                                        String status = requestData.get("requeststatus").toString();
                                        requestStatus.setText(status);
                                        requestBookTitle.setText(title);
                                        requestBookAuthor.setText(author);

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

        return view;
    }

    /**
     * Gets the number of the requests
     * @return
     */
    public int getCount() {
        return myRequests.size();
    }
}
