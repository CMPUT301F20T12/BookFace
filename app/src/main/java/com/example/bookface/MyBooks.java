package com.example.bookface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * This class displays the list of the books owned by the user
 */
public class MyBooks extends AppCompatActivity {

    // Variable declarations
    RecyclerView recycleView;
    ArrayList<String> myBookList;
    RecyclerViewAdapter adapter;
    Button addBookButton;

    FirebaseAuth mFirebaseAuth;
    FirebaseUser userInstance;
    private BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.my_books);

            mFirebaseAuth = FirebaseAuth.getInstance();
            userInstance = mFirebaseAuth.getCurrentUser();
            myBookList = new ArrayList<>();
            addBookButton = (Button) findViewById(R.id.add_book);

            Context context = this;
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
                            System.out.println(myBookList);


                            recycleView = findViewById(R.id.recycle_view);
                            adapter = new RecyclerViewAdapter(context, myBookList);
                            recycleView.setAdapter(adapter);
                            recycleView.setLayoutManager(new LinearLayoutManager(context));

                            navBar = findViewById(R.id.nav_bar);

                            navBar.setOnNavigationItemSelectedListener(navBarMethod);
                        }
                    }
                }
            });
        }

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAddEditBooks = new Intent(MyBooks.this, AddEditBookActivity.class);
                startActivity(toAddEditBooks);
            }
        });
    }

    /**
     * This is used to setup the bottom navigation bar
     */
    private  BottomNavigationView.OnNavigationItemSelectedListener navBarMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
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
            }
            return false;
        }
    };
}
