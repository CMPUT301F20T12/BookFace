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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This is the class that is responsible for the search activity
 */
public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    // Declare variables
    ListView bookListView;
    ArrayAdapter<Book> bookListAdapter;
    ArrayList<Book> bookDataList;
    SearchView editSearch;
    private BottomNavigationView navBar;
    private static final String TAG = "SEARCH";

    FirestoreController mFirestoreController;

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

        fetchBooks();

        editSearch = findViewById(R.id.search_bar);
        editSearch.setOnQueryTextListener(this);
        
        // Set up on ItemClickListener for the books in the activity
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, BookDescription.class);
                Book book = bookListAdapter.getItem(position);
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
        }
        // if search is cleared
        else {
            fetchBooks();
            bookListAdapter.getFilter().filter("");
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // if search is cleared
        if (newText.length() == 0) {
            fetchBooks();
            bookListAdapter.getFilter().filter("");
        }
        return false;
    }

    /**
     * This method is used to fetch the books from the firebase
     */
    private void fetchBooks() {
        mFirestoreController = new FirestoreController();
        final CollectionReference bookReference = mFirestoreController.getCollRef("books");
        bookReference
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Book book = doc.toObject(Book.class);
                            // add from FireStore
                            bookDataList.add(book);
                        }
                        bookListAdapter.notifyDataSetChanged();
                    } 
                    else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }
    
    /**
     * This is used to implement the bottom navigation bar
     */
    private  BottomNavigationView.OnNavigationItemSelectedListener navBarMethod =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.my_books:
                    Intent toMyBooks = new Intent(SearchActivity.this, MyBooks.class);
                    startActivity(toMyBooks);
                    break;
                case R.id.profile:
                    Intent toMyProfile = new Intent(SearchActivity.this, UserProfileActivity.class);
                    startActivity(toMyProfile);
                    break;
            }
            return false;
        }
    };
}
