package com.example.bookface;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyBookList extends ArrayAdapter<String>{

    private ArrayList<String> books;
    private Context context;

    public MyBookList(Context context, ArrayList<String> books){
        super(context,0, books);
        this.books = books;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.book, parent,false);
        }

        String book = books.get(position);

        TextView isbn = view.findViewById(R.id.isbn);
//        TextView provinceName = view.findViewById(R.id.province_text);

        isbn.setText(book);
//        provinceName.setText(city.getProvinceName());

        return view;

    }
}
