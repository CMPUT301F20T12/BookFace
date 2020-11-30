package com.example.bookface;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Locale;

import javax.annotation.Nullable;

/**
 * This is the class that is used to make the custom list of array of the Books
 */
public class BookList extends ArrayAdapter<Book> implements Filterable {

    private ArrayList<Book> books;
    private ArrayList<Book> originalBooks;
    private Context context;
    private Filter filter;

    /**
     * This is the constructor
     * @param context
     * The context of the app
     * @param books
     * This is the array of the books
     */
    public BookList(Context context, ArrayList<Book> books){
        super(context,0, books);
        this.books = books;
        this.context = context;
    }

    /**
     * Sets up the custom array adapter
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
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

    /**
     * This method returns the count of the number of books
     * @return
     * The number of books inside the book list
     */
    public int getCount() {
        return books.size();
    }

    /**
     * Override the getItem method to return book at proper position when filtered
     * @param position
     * @return
     */
    @Override
    public Book getItem(int position) {
        return books.get(position);
    }

    /**
     * Override the getFilter method
     * @return
     * The filter
     */
    @Override
    public Filter getFilter() {
        filter = new BookFilter();

        return filter;
    }

    /**
     * This class provides a filter for the book list adapter -- used in search
     */
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

        /**
         * Override the performFiltering method
         * @param searchTerm
         * @return filterResults
         */
        @Override
        protected FilterResults performFiltering(CharSequence searchTerm) {
            FilterResults filterResults = new FilterResults();
            ArrayList<Book> tempBooks = new ArrayList<>();

            if (originalBooks == null) {
                originalBooks = new ArrayList<>(books);
            }

            if (searchTerm == null || searchTerm.length() == 0) {
                filterResults.count = originalBooks.size();
                filterResults.values = originalBooks;
            }
            else {
                int length = books.size();
                for (int i = 0; i < length; i++) {
                    Book book = books.get(i);
                    if (book.getTitle().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getAuthor().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getISBN().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getDescription().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getOwnerUsername().toLowerCase(Locale.getDefault()).contains(searchTerm) ||
                        book.getBorrowerUsername().toLowerCase(Locale.getDefault()).contains(searchTerm)) {
                        // search through the fields of a book
                        tempBooks.add(book);
                    }
                }
                filterResults.values = tempBooks;
                filterResults.count = tempBooks.size();
            }

            return filterResults;
        }
    }
}
