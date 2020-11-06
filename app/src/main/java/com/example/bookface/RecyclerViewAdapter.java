package com.example.bookface;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> myBooks;
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<String> myBooks) {
        this.myBooks = myBooks;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        final String bookISBN = (myBooks.get(position)).trim();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String docPath = "books/".concat(bookISBN);
        DocumentReference docRef = db.document(docPath);
        final ViewHolder newHolder = holder;

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    System.out.println("GETTING BOOK DATA");
                    System.out.println(document);
                    if (document.exists()) {
                        Map bookData = document.getData();
                        System.out.println("BOOK DATA ----> "+bookData);
                        if(bookData != null){
                            String title = bookData.get("title").toString();
                            String author = bookData.get("author").toString();
                            String status = bookData.get("status").toString();
                            String isbn = bookData.get("isbn").toString();

                            holder.title.setText(title);
                            holder.isbn.setText(isbn);
                            holder.author.setText(author);
                            holder.status.setText(status);

                            Log.d(TAG, "INSIDE THE SUCCESS CONDITION");

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

    @Override
    public int getItemCount() {
        if(this.myBooks!=null){
            return this.myBooks.size();
        }
        else{
            return 0;
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView isbn;
        TextView author;
        TextView status;

        public ViewHolder(View view) {
            super(view);
            isbn = view.findViewById(R.id.isbn);
            title = view.findViewById(R.id.title);
            author = view.findViewById(R.id.author);
            status = view.findViewById(R.id.status);

        }
    }


}

