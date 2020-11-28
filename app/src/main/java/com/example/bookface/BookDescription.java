package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Map;

/**
 * This class is the activity that displays info of the book selected
 */
public class BookDescription extends AppCompatActivity {

    private static final String TAG = "BOOK_DESC_MSG";
    // Declare the fireAuth variable to get the currentUser()
    FirebaseAuth mFirebaseAuth;
    FirebaseUser userInstance;
    FirestoreController mFirestoreController;

    // Declaration of some variables
    String owner;
    String author;
    String borrower;
    String title;
    String description;
    String status;
    String isbn;
    String bookId;
    String imgUrl;
    String currentUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_book_description);
        setContentView(R.layout.activity_bookdescription_updated);

        // Retrieve the objects passed into the intent object
        mFirebaseAuth = FirebaseAuth.getInstance();
        userInstance = mFirebaseAuth.getCurrentUser();

        Button btnEdit = findViewById(R.id.editButton);
        Button btnTop = findViewById(R.id.myBooksOrSearchButton);
        Button btnBottom = findViewById(R.id.collectOrSendRequestButton);

        TextView textAuthor = (TextView) findViewById(R.id.authorNameText);
        TextView textTitle = (TextView) findViewById(R.id.titleText);
        TextView textIsbn = (TextView) findViewById(R.id.isbnText);
        TextView textStatus = (TextView) findViewById(R.id.statusText);
//        TextView textDescHeading = (TextView) findViewById(R.id.descriptionHeadingText);
        TextView textDescription = (TextView) findViewById(R.id.bookDescriptionText);
        TextView textBorrower = (TextView) findViewById(R.id.borrowerNameText);
        TextView textOwner = (TextView) findViewById(R.id.ownerNameText);
        ImageView image = (ImageView) findViewById(R.id.imageView);

        if (userInstance != null){
            currentUser = (String) userInstance.getDisplayName();

            // Get the value passed while intenting
            Bundle b = getIntent().getExtras();
            System.out.println(b.get("BOOK_ID"));
            if (b!= null) {
                bookId = (String) b.get("BOOK_ID");
            }
            mFirestoreController = new FirestoreController();

            // Firebase document listener
            final DocumentReference docRef = mFirestoreController.getDocRef("books", bookId);
          
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null && value.exists() && value != null) {
                        Book book = value.toObject(Book.class);
                        owner = book.getOwnerUsername();
                        author = book.getAuthor();
                        isbn = book.getISBN();
                        description = book.getDescription();
                        status = book.getStatus();
                        title = book.getTitle();
                        imgUrl = book.getImageUrl();
                        String borrowerUsername = book.getBorrowerUsername();

                        if (borrowerUsername == null) {
                            borrower = "No current borrower";
                        } 
                        else {
                            borrower = borrowerUsername;
                        }

                        textAuthor.setText(author);
                        textIsbn.setText(isbn);
                        textStatus.setText(status);
                        textTitle.setText(Html.fromHtml("<b>" + title + "</b>"));
                        textDescription.setText(description);
                        textBorrower.setText(borrower);
                        textOwner.setText("@".concat(owner));
                        if(imgUrl!="") {
                            Picasso.with(getApplicationContext()).load(imgUrl).into(image);
                      
                            // Show the enlarged image
                            image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent toZoom = new Intent(BookDescription.this, ZoomActivity.class);
                                    toZoom.putExtra("imgURL", imgUrl);
                                    startActivity(toZoom);
                                }
                            });
                        }
                    }

                    textOwner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Call MyBooks Activity
                            DocumentReference docRef = mFirestoreController.getDocRef("users", owner);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            User user = document.toObject(User.class);
                                            if(user != null){
                                                String email = user.getEmail();
                                                String contact = user.getContactNo();

                                                Bundle bundle = new Bundle();
                                                bundle.putString("USERNAME", owner);
                                                bundle.putString("USER_EMAIL", email);
                                                bundle.putString("USER_CONTACT", contact);

                                                UserProfileFragment userProfileFragment = new UserProfileFragment();
                                                userProfileFragment.setArguments(bundle);
                                                userProfileFragment.show(getSupportFragmentManager(),"userProfileFragment");
                                            }
                                        } 
                                        else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } 
                                    else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });
                        }
                    });

                  
                    // If the current user is the owner of the book, set the fields and buttons' visibility accordingly
                    if (owner.equals(currentUser)) {
                        btnEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Call Add/Edit Book activity
                                Intent toAddEditBooks = new Intent(BookDescription.this, AddEditBookActivity.class);
                                toAddEditBooks.putExtra("Book", bookId);
                                startActivity(toAddEditBooks);
                            }
                        });

                        btnTop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Call MyBooks Activity
                                finish();
                            }
                        });

                        btnBottom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Call the functionality to collect the book
                            }
                        });
                    }
                    // If the owner is not the current user, set the fields differently
                    else {
                        btnTop.setText("Search");
                        btnEdit.setVisibility(View.INVISIBLE);
                        btnBottom.setText("Send Request");
                        btnTop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Call Search Books Activity
                                finish();
                            }
                        });

                        btnBottom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Call Send Request Activity
                                openDialog();
                            }
                        });
                        textBorrower.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    public void openDialog() {
        SendRequestDialog sendRequestDialog = new SendRequestDialog();
        sendRequestDialog.show(getSupportFragmentManager(), "Send Request");
    }
}
