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

/**
 * This is a class to implement the on item click listener in the recyclerView in myBooks activity
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    // Variable declarations
    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<String> myBooks;
    private Context context;
    private OnBookClickListener onBookClickListener;

    /**
     * This is the constructor
     * @param context
     * @param myBooks
     */
    public RecyclerViewAdapter(Context context, ArrayList<String> myBooks, OnBookClickListener onBookClickListener) {
        this.myBooks = myBooks;
        this.context = context;
        this.onBookClickListener=onBookClickListener;
    }

    /**
     * This is a system method that creates a View holder
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book, parent, false);
        ViewHolder holder = new ViewHolder(view, onBookClickListener);
        return holder;
    }

    /**
     * For each book reference, the book data is set accordingly to the fields
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");

        final String bookId = (myBooks.get(position)).trim();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String docPath = "books/".concat(bookId);
        DocumentReference docRef = db.document(docPath);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map bookData = document.getData();
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

    /**
     * This retrieves the number of books
     * @return
     * the number of books
     */
    @Override
    public int getItemCount() {
        if(this.myBooks!=null){
            return this.myBooks.size();
        }
        else{
            return 0;
        }

    }

    /**
     * This class is being used above to set the fields
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Variable declarations
        TextView title;
        TextView isbn;
        TextView author;
        TextView status;
        OnBookClickListener onBookClickListener;

        /**
         * This is the constructor
         * @param view
         */
        public ViewHolder(View view, OnBookClickListener onBookClickListener) {
            super(view);
            isbn = view.findViewById(R.id.book_isbn);
            title = view.findViewById(R.id.book_title);
            author = view.findViewById(R.id.book_author);
            status = view.findViewById(R.id.book_status);
            this.onBookClickListener = onBookClickListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onBookClickListener.onBookClick(getAdapterPosition());
        }
    }

    /**
     * Implement the methods from the interface
     */
    public interface OnBookClickListener{
        void onBookClick(int position);
    }

}