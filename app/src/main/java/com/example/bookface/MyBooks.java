package com.example.bookface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class displays the list of the books owned by the user
 */
public class MyBooks extends AppCompatActivity implements RecyclerViewAdapter.OnBookClickListener, FilterBooksDialog.OnFragmentInteractionListener, Filterable {

    // Variable declarations
    RecyclerView recycleView;
    ArrayList<String> myBookList;
    ArrayList<String> filteredBookList;
    ArrayList<String> originalBooks;
    RecyclerViewAdapter adapter;
    Button addBookButton;
    Button FilterBooksButton;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser userInstance;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_books);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userInstance = mFirebaseAuth.getCurrentUser();
        recycleView = findViewById(R.id.recycle_view);
        myBookList = new ArrayList<>();

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycleView);

        Context context = this;
        RecyclerViewAdapter.OnBookClickListener onBookClickListener = this;
        if (userInstance != null){
            String userName = userInstance.getDisplayName();

            // Find the user document from the firebase
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String docPath = "users/"+userName;
            DocumentReference docRef = db.document(docPath);

            // Read the user document to retrieve all the books he/she owns
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // set the books in the recyclerView
                            myBookList = (ArrayList<String>)document.get("booksOwned");

                            adapter = new RecyclerViewAdapter(context, myBookList, onBookClickListener);
                            recycleView.setAdapter(adapter);
                            recycleView.setLayoutManager(new LinearLayoutManager(context));

                            navBar = findViewById(R.id.nav_bar);

                            navBar.setOnNavigationItemSelectedListener(navBarMethod);
                        }
                    }
                }
            });
        }

        addBookButton = findViewById(R.id.add_book);
        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddEditBooks = new Intent(MyBooks.this, AddEditBookActivity.class);
                startActivity(toAddEditBooks);
            }
        });
        FilterBooksButton = findViewById(R.id.button_filter);
        FilterBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment filterBooksDialog = new FilterBooksDialog();
                filterBooksDialog.show(getSupportFragmentManager(), "missiles");
            }
        });
    }

    // On swipe, delete the book
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int index = viewHolder.getAdapterPosition();
            String bookId = myBookList.get(index);

            final DocumentReference docRef = db.collection("books").document(bookId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Map bookData = document.getData();
                            if (bookData != null) {
                                String status = bookData.get("status").toString();
                                ArrayList<DocumentReference> requestList = (ArrayList<DocumentReference>)
                                        bookData.get("requestlist");
                                String owner = bookData.get("ownerUsername").toString();
                                if(status.toLowerCase().equals("available")  == false) {
                                    Toast.makeText(MyBooks.this,
                                            "Delete request denied, retry when book is available!",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else if(requestList.size() != 0) {
                                    Toast.makeText(MyBooks.this,
                                            "Delete request denied, retry when no incoming requests on the book!",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    myBookList.remove(bookId);
                                    docRef.delete();
                                    final DocumentReference docRefUser = db.collection("users").document(owner);
                                    docRefUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Map userData = document.getData();
                                                    if (userData != null) {
                                                        ArrayList<DocumentReference> ownedBooks =
                                                                (ArrayList<DocumentReference>) userData.get("booksOwned");
                                                        ownedBooks.remove(bookId);
                                                        docRefUser.update("booksOwned", ownedBooks);
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }

            });
        }
    };

    /**
     * This is used to setup the bottom navigation bar
     */
    private  BottomNavigationView.OnNavigationItemSelectedListener navBarMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.profile:
                    Intent toMyProfile = new Intent(MyBooks.this, UserProfileActivity.class);
                    startActivity(toMyProfile);
                    break;
                case R.id.search:
                    Intent toSearch = new Intent(MyBooks.this, SearchActivity.class);
                    startActivity(toSearch);
                    break;
                case R.id.requests:
                    Intent toMyRequests = new Intent(MyBooks.this, MyRequestsActivity.class);
                    startActivity(toMyRequests);
                    break;
            }
            return false;
        }
    };

    /**
     * This is basically the onItemClick listener
     * @param position
     */
    @Override
    public void onBookClick(int position) {
        String bookId = myBookList.get(position);
        Intent intent = new Intent(MyBooks.this, BookDescription.class);
        intent.putExtra("BOOK_ID", bookId);
        startActivity(intent);
    }

    @Override
    public void onStatusSelected(String status) {
        getFilter().filter(status);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence status) {
                FilterResults filterResults = new FilterResults();

                if (originalBooks == null) {
                    originalBooks = new ArrayList<>(myBookList);
                }

                if (status == "All") {
                    filterResults.count = originalBooks.size();
                    filterResults.values = originalBooks;
                } else {
                    ArrayList<String> filteredList = new ArrayList<>();
                    for (String bookId : myBookList) {
                        String docPath = "books/"+bookId;
                        DocumentReference bookRef = db.document(docPath);
                        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if(documentSnapshot !=null){
                                        Book book = documentSnapshot.toObject(Book.class);

                                        if (status == "Available" && book.getStatus() == "Available") {
                                            filteredList.add(bookId);
                                        } else if (status == "Requested" && book.getStatus() == "Requested") {
                                            filteredList.add(bookId);
                                        } else if (status == "Accepted" && book.getStatus() == "Accepted") {
                                            filteredList.add(bookId);
                                        } else if (status == "Borrowed" && book.getStatus() == "Borrowed") {
                                            filteredList.add(bookId);
                                        }
                                        filteredBookList = filteredList;
                                    }
                                }
                            }
                        });

                    }

                }

                filterResults.values = filteredBookList;
                return filterResults;
            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                return null;
//            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredBookList = (ArrayList<String>) filterResults.values;
                myBookList = filteredBookList;

                // refresh the list with filtered data
                adapter.notifyDataSetChanged();
            }
        };
    }
}