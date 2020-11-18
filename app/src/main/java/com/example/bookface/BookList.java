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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;

public class BookList extends ArrayAdapter<Book> {

    private ArrayList<Book> books;
    private Context context;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser userInstance;

    public BookList(Context context, ArrayList<Book> books){
        super(context,0, books);
        this.books = books;
        this.context = context;
    }



//    public void filter(String charText) {
//        charText = charText.toLowerCase(Locale.getDefault());
//        animalNamesList.clear();
//        if (charText.length() == 0) {
//            animalNamesList.addAll(arraylist);
//        } else {
//            for (AnimalNames wp : arraylist) {
//                if (wp.getAnimalName().toLowerCase(Locale.getDefault()).contains(charText)) {
//                    animalNamesList.add(wp);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.book, parent,false);
        }

        final Book book = books.get(position);

        TextView bookTitle = view.findViewById(R.id.book_title);
        TextView bookAuthor = view.findViewById(R.id.book_author);
        TextView ISBN = view.findViewById(R.id.book_isbn);
        TextView status = view.findViewById(R.id.book_status);

        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        ISBN.setText(book.getISBN());
        status.setText(book.getStatus());

        mFirebaseAuth = FirebaseAuth.getInstance();
        userInstance = mFirebaseAuth.getCurrentUser();
        if (userInstance != null){
            String userName = userInstance.getDisplayName();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            final CollectionReference booksReference = db.collection("books");
        }

        return view;
    }
}
