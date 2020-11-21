package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * This class is the activity that displays info of the book selected
 */
public class BookDescription extends AppCompatActivity {

    // Declare the fireAuth variable to get the currentUser()
    FirebaseAuth mFirebaseAuth;
    FirebaseUser userInstance;

    // Declaration of some variables
    String owner;
    String author;
    String borrower;
    String title;
    String description;
    String status;
    String isbn;
    String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);

        // Retrieve the objects passed into the intent object
        // TODO
        mFirebaseAuth = FirebaseAuth.getInstance();
        userInstance = mFirebaseAuth.getCurrentUser();

        Button btnEdit = findViewById(R.id.editButton);
        Button btnTop = findViewById(R.id.myBooksOrSearchButton);
        Button btnBottom = findViewById(R.id.collectOrSendRequestButton);

        TextView textAuthor = findViewById(R.id.authorNameText);
        TextView textTitle = findViewById(R.id.titleText);
        TextView textIsbn = findViewById(R.id.isbnText);
        TextView textStatus = findViewById(R.id.statusText);
        TextView textDescHeading = findViewById(R.id.descriptionHeadingText);
        TextView textDescription = findViewById(R.id.bookDescriptionText);
        TextView textBorrower = findViewById(R.id.borrowerNameText);
        ImageView image = (ImageView) findViewById(R.id.imageView);

        String currentUser = null;

        if (userInstance != null){
            currentUser = userInstance.getDisplayName();

            isbn = (String) savedInstanceState.getSerializable("Book");

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            final DocumentReference docRef = db.collection("books").document(isbn);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null && value.exists() && value != null) {
                        owner = value.getString("ownerUsername");
                        borrower = value.getString("borrowerUsername");
                        author = value.getString("author");
                        description = value.getString("description");
                        status = value.getString("status");
                        title = value.getString("title");
                        imgUrl = value.getString("imageUrl");
                    }
                }
            });
        }
        
        // TODO
        if (owner != currentUser) {
            btnTop.setText("Search");
            btnEdit.setVisibility(View.INVISIBLE);
            btnBottom.setText("Send Request");
            btnTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call Search Books Activity
                }
            });

            btnBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call Send Request Activity
                }
            });
            textBorrower.setVisibility(View.INVISIBLE);
        }
        else {
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call Add/Edit Book activity
                    Intent toAddEditBooks = new Intent(BookDescription.this, AddEditBookActivity.class);
                    toAddEditBooks.putExtra("isbnNumber", isbn);
                    startActivity(toAddEditBooks);
                }
            });

            btnTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call MyBooks Activity
                    Intent toMyBooks = new Intent(BookDescription.this, MyBooks.class);
                    startActivity(toMyBooks);
                }
            });

            btnBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call the functionality to collect the book
                }
            });
            textBorrower.setText(borrower);
        }

        // TODO
        textAuthor.setText(author);
        textDescHeading.setText(Html.fromHtml("<b>DESCRIPTION:</b>"));
        textIsbn.setText(isbn);
        textDescription.setText(description);
        textStatus.setText(status);
        textTitle.setText(Html.fromHtml("<b>" + title + "</b>"));

        Picasso.with(this).load(imgUrl).into(image);

    }


//    public void clickImage(View view) {
//        Intent showImage = new Intent(BookDescription.this, AddImage.class);
//        startActivity(showImage);
//    }
}