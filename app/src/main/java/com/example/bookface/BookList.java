package com.example.bookface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

/**
 * This is a class that keeps track of a list of book objects
 */
public class BookList extends ArrayAdapter<Book> {
    private ArrayList<Book> books;
    private Context context;

    /**
     * Constructor for the book list class
     * @param context
     * @param books
     */
    public BookList(Context context, ArrayList<Book> books) {
        super(context, 0, books);
        this.books = books;
        this.context = context;
    }

    /**
     * To create the custom list adapter
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Nullable
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.book_list, null);
        }

        final Book book = books.get(position);

        ImageView bookImage = (ImageView) view.findViewById(R.id.bookImage);
        TextView bookTitle = (TextView) view.findViewById(R.id.bookTitle);
        TextView bookDescription = (TextView) view.findViewById(R.id.bookDescription);
        TextView statusAndUser = (TextView) view.findViewById(R.id.statusAndUser);

        // TODO: get book title, description, status, and user from firestore
        bookTitle.setText(book.getTitle());
        bookDescription.setText(book.getDescription());
        String statusAndUserString = String.format("%s | %s", book.getStatus(), "Username");
        statusAndUser.setText(statusAndUserString);

        return view;
    }
}
