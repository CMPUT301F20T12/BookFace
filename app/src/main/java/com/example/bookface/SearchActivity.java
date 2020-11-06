package com.example.bookface;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
  ArrayList<Book> books = new ArrayList<>();
  BookList bookListAdapter;
  ListView bookListView;
  FirebaseFirestore db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    db = FirebaseFirestore.getInstance();

    bookListAdapter = new BookList(this, books);
    bookListView = (ListView) findViewById(R.id.bookList);
    bookListView.setAdapter(bookListAdapter);

    final CollectionReference collectionReference = db.collection("books");
  }
}
