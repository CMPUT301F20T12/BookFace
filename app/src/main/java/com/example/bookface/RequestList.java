package com.example.bookface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This is a class that keeps track of a list of request objects
 */
public class RequestList extends ArrayAdapter<Request> {

    private ArrayList<Request> requests;
    private Context context;

    public RequestList(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);
        this.requests = requests;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.request, parent, false);
        }

        final Request request = requests.get(position);
        final Book requestedBook = request.getBookRequested();

        TextView bookTitle = view.findViewById(R.id.request_book_title);
        TextView bookAuthor = view.findViewById(R.id.request_book_author);
        TextView status = view.findViewById(R.id.request_status);

        bookTitle.setText(requestedBook.getTitle());
        bookAuthor.setText(requestedBook.getAuthor());
        status.setText(request.getRequestStatus());

        return view;
    }

    public int getCount() {
        return requests.size();
    }
}

