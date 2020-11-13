package com.example.bookface;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
  ArrayList<Book> books = new ArrayList<>();
  BookList bookListAdapter;
  ListView bookListView;
  FirebaseFirestore db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    // Initialize the book list
    bookListAdapter = new BookList(this, books);
    bookListView = (ListView) findViewById(R.id.bookList);
    bookListView.setAdapter(bookListAdapter);


  }

  @Override
  public boolean onQueryTextSubmit(String query) {

    return false;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    String text = newText;
    bookListAdapter.searchForBooks(text);
    return false;
  }

  /**
   * The method to do the searching
   * @param searchTerm - the term entered to search bar
   * @return true if results were found, false if not
   */
  public boolean searchForBook(String searchTerm) {
    // TODO: implement search logic here
    db = FirebaseFirestore.getInstance();
    final CollectionReference collectionReference = db.collection("books");

    return true;
  }
}
