package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

// on click method for username to display profile

public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
  private static final String TAG = "SEARCH";
  ListView bookListView;
  ArrayAdapter<Book> bookListAdapter;
  ArrayList<Book> bookDataList;
  FirebaseFirestore db;

  SearchView editSearch;
  private BottomNavigationView navBar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    FirebaseFirestore db;

    navBar = findViewById(R.id.nav_bar);
    // navBar.setOnNavigationItemSelectedListener(navBarMethod);

    // Initialize the book list
    bookListView = (ListView) findViewById(R.id.bookList);
    bookDataList = new ArrayList<>();

    bookListAdapter = new BookList(this, bookDataList);
    bookListView.setAdapter(bookListAdapter);

    db = FirebaseFirestore.getInstance();
    final CollectionReference bookReference = db.collection("books");

    bookReference
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
          @Override
          public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
              for (QueryDocumentSnapshot doc : task.getResult()) {
                Log.d(TAG, doc.getId() + " => " + doc.getData());
                String title = (String) doc.getData().get("title");
                String author = (String) doc.getData().get("author");
                String ISBN = doc.getId();
                String description = (String) doc.getData().get("description");
                String status = (String) doc.getData().get("status");
                String ownerUsername = (String) doc.getData().get("ownerUsername");
                String borrowerUsername = (String) doc.getData().get("borrowerUsername");
                String imageUrl = (String) doc.getData().get("imageUrl");
                bookDataList.add(new Book(title, author, ISBN, description,
                    status, ownerUsername, borrowerUsername, imageUrl)); // add from FireStore
              }
              bookListAdapter.notifyDataSetChanged();
            } else {
              Log.d(TAG, "Error getting documents: ", task.getException());
            }
          }
        });

    editSearch = findViewById(R.id.search_bar);
    editSearch.setOnQueryTextListener(this);
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
//    // retrieve search results
//    if (query.length() > 0) {
//      bookListAdapter.getFilter().filter(query);
//    }
    return false;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    if (newText.length() > 0) {
      bookListAdapter.getFilter().filter(newText);
    }
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
