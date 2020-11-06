package com.example.bookface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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

public class MyBooks extends AppCompatActivity {

    RecyclerView recycleView;
    ArrayList<String> myBookList;
    RecyclerViewAdapter adapter;

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

        Context context = this;
        if (userInstance != null){
            String userName = userInstance.getDisplayName();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String docPath = "users/"+userName;
            DocumentReference docRef = db.document(docPath);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            myBookList = (ArrayList<String>)document.get("booksOwned");
                            System.out.println(myBookList);

//                            new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recycleView);

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
        };

//    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
//        @Override
//        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//            return false;
//            }
//        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//
//            }
//        };


    private  BottomNavigationView.OnNavigationItemSelectedListener navBarMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


            switch (menuItem.getItemId()){
                case R.id.profile:
                    Intent toMyProfile = new Intent(MyBooks.this, LoginConfirmationActivity.class);
                    startActivity(toMyProfile);
                    break;
                case R.id.requests:
                    Intent toRequests = new Intent(MyBooks.this, SignupActivity.class);
                    startActivity(toRequests);
                    break;
                case R.id.search:
                    Intent toSearch = new Intent(MyBooks.this, SignupActivity.class);
                    startActivity(toSearch);
                    break;
                case R.id.notification:
                    Intent toNotification = new Intent(MyBooks.this, SignupActivity.class);
                    startActivity(toNotification);
                    break;
            }
            return false;
        }
    };
}
