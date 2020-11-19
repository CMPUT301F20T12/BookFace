package com.example.bookface;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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

public class BookList extends ArrayAdapter<Book> implements Filterable{

    private ArrayList<Book> books;
    private Context context;
    private Filter filter;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser userInstance;

    public BookList(Context context, ArrayList<Book> books){
        super(context,0, books);
        this.books = books;
        this.context = context;
    }

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

        return view;
    }

    public int getCount() {
        return books.size();
    }

    @Override
    public Filter getFilter() {
        filter = new BookFilter();

        return filter;
    }

    public class BookFilter extends Filter {
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence searchTerm, FilterResults filterResults) {
            ArrayList<Book> filteredBooks = (ArrayList<Book>) filterResults.values;

            if (filterResults.count > 0) {
                books = filteredBooks;
                Log.d("SEARCH", String.valueOf(books.size()));
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence searchTerm) {
            FilterResults filterResults = new FilterResults();
            ArrayList<Book> tempBooks = new ArrayList<>();

            if (searchTerm != null && books != null) {
                int length = books.size();
                for (int i = 0; i < length; i++) {
                    Book book = books.get(i);
                    if (book.getTitle().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getAuthor().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getISBN().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getDescription().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getOwnerUsername().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getBorrowerUsername().toLowerCase(Locale.getDefault()).contains(searchTerm)) { // search through the fields of a book
                        tempBooks.add(book);
                    }
                }
            } else if (searchTerm.length() == 0 || searchTerm == null) {
                tempBooks.addAll(books);
            }

            filterResults.values = tempBooks;
            filterResults.count = tempBooks.size();
            return filterResults;
        }
    }
}
