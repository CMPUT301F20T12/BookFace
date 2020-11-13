package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

// on click method for username to display profile

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
  ArrayList<Book> books = new ArrayList<>();
  BookList bookListAdapter;
  ListView bookListView;
  FirebaseFirestore db;

  SearchView editSearch;
  private BottomNavigationView navBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    navBar = findViewById(R.id.nav_bar);
    // navBar.setOnNavigationItemSelectedListener(navBarMethod);

    // Initialize the book list
    bookListAdapter = new BookList(this, books);
    bookListView = (ListView) findViewById(R.id.bookList);
    bookListView.setAdapter(bookListAdapter);

    editSearch = (SearchView) findViewById(R.id.search_bar);
    editSearch.setOnQueryTextListener(this);

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


//  private  BottomNavigationView.OnNavigationItemSelectedListener navBarMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//
//      switch (menuItem.getItemId()){
//        case R.id.profile:
//          Intent toMyProfile = new Intent(SearchActivity.this, UserProfileActivity.class);
//          startActivity(toMyProfile);
//          break;
////                case R.id.requests:
////                    Intent toRequests = new Intent(MyBooks.this, SignupActivity.class);
////                    startActivity(toRequests);
////                    break;
//        case R.id.my_books:
//          Intent toMyBooks = new Intent(SearchActivity.this, MyBooks.class);
//          startActivity(toMyBooks);
//          break;
////                case R.id.notification:
////                    Intent toNotification = new Intent(MyBooks.this, SignupActivity.class);
////                    startActivity(toNotification);
////                    break;
//      }
//      return false;
//    }
//  };
}
