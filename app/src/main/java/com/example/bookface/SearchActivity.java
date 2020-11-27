package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

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

    navBar = findViewById(R.id.nav_bar);

    navBar.setOnNavigationItemSelectedListener(navBarMethod);

    // Initialize the book list
    bookListView = findViewById(R.id.bookList);
    bookDataList = new ArrayList<>();

    bookListAdapter = new BookList(this, bookDataList);
    bookListView.setAdapter(bookListAdapter);

    db = FirebaseFirestore.getInstance();
    fetchBooks(db);

    editSearch = findViewById(R.id.search_bar);
    editSearch.setOnQueryTextListener(this);

    bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(SearchActivity.this, BookDescription.class);
        Book book = bookDataList.get(position);
        String bookId = book.getISBN()+book.getOwnerUsername();
        System.out.println("On click book id --> "+bookId);
        intent.putExtra("BOOK_ID", bookId);
        startActivity(intent);
      }
    });
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    // retrieve search results on submit
    if (query.length() > 0) {
      bookListAdapter.getFilter().filter(query);
    } else { // if search is cleared
      fetchBooks(db);
      bookListAdapter.getFilter().filter("");
    }
    return false;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    // if search is cleared
    if (newText.length() == 0) {
      fetchBooks(db);
      bookListAdapter.getFilter().filter("");
    }
    return false;
  }

  private void fetchBooks(FirebaseFirestore db) {
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
                String ISBN = (String) doc.getData().get("isbn");
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
  }

  private  BottomNavigationView.OnNavigationItemSelectedListener navBarMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

      switch (menuItem.getItemId()){
        case R.id.my_books:
          Intent toMyBooks = new Intent(SearchActivity.this, MyBooks.class);
          startActivity(toMyBooks);
          break;
        case R.id.requests:
          Intent toRequests = new Intent(SearchActivity.this, MyRequestsActivity.class);
          startActivity(toRequests);
          break;
        case R.id.profile:
          Intent toMyProfile = new Intent(SearchActivity.this, UserProfileActivity.class);
          startActivity(toMyProfile);
          break;
//                case R.id.notification:
//                    Intent toNotification = new Intent(LoginConfirmationActivity.this, SignupActivity.class);
//                    startActivity(toNotification);
//                   s break;
      }
      return false;
    }
  };
}
