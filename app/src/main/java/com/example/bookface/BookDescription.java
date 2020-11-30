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
public class BookDescription extends AppCompatActivity implements SendRequestDialog.OnFragmentInteractionListener{

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
            System.out.println("BOOKS ID: "+bookId);
            mFirestoreController = new FirestoreController();

            // Firebase document listener
            final DocumentReference docRef = mFirestoreController.getDocRef("books", bookId);
          
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null && value.exists() && value != null) {
                        owner = value.getString("ownerUsername");
                        author = value.getString("author");
                        isbn = value.getString("isbn");
                        description = value.getString("description");
                        status = value.getString("status");
                        title = value.getString("title");
                        imgUrl = value.getString("imageUrl");

                        borrower = value.get("borrowerUsername").toString();

                        textAuthor.setText(author);
                        textIsbn.setText(isbn);
                        textStatus.setText(status);
                        textTitle.setText(Html.fromHtml("<b>" + title + "</b>"));
                        textDescription.setText(description);
                        textBorrower.setText(borrower);
                        textOwner.setText(owner);
                        if(imgUrl.equals("") == false) {
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
                            DocumentReference docRef = mFirestoreController.getDocRef("users", owner);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Map userData = document.getData();
                                            if(userData != null){
                                                String email = userData.get("email").toString();
                                                String contact = userData.get("contactNo").toString();

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

                    if(borrower.toLowerCase().equals("null") == false){
                        textBorrower.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DocumentReference docRef = mFirestoreController.getDocRef("users", borrower);
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Map userData = document.getData();
                                                if(userData != null){
                                                    String email = userData.get("email").toString();
                                                    String contact = userData.get("contactNo").toString();

                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("USERNAME", borrower);
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
                    }
                  
                    // If the current user is the owner of the book, set the fields and buttons' visibility accordingly
                    if (owner.equals(currentUser)) {
                        if(status.equals("Available")){
                            btnBottom.setText("View Requests");
                            btnBottom.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(BookDescription.this, ViewRequestActivity.class);
                                    intent.putExtra("BOOK_ID",isbn.concat(owner));
                                    System.out.println("ISBN  "+isbn);
                                    startActivity(intent);
                                }
                            });
                        }
                        else{
                            if(status.equals("Accepted")){
                                btnBottom.setText("Handover");
                            }
                            else if(status.equals("Borrowed")){
                                btnBottom.setText("Receive Back");
                            }
                            btnBottom.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final DocumentReference docRef = mFirestoreController.getDocRef("books", bookId);
                                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                  if (task.isSuccessful()) {
                                                       DocumentSnapshot document = task.getResult();
                                                            if (document.exists()) {
                                                                 Map bookData = document.getData();
                                                                 if (bookData != null) {
                                                                       ArrayList<DocumentReference> requestList = (ArrayList<DocumentReference>) bookData.get("requestlist");
                                                                       Intent intent = new Intent(BookDescription.this, BookExchangeDisplayActivity.class);
                                                                       intent.putExtra("REQUEST_ID", requestList.get(0).getId());
                                                                       startActivity(intent);
                                                                 }
                                                            }
                                                       }
                                        }
                                    });
                                }
                            });
                        }
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

//                        btnBottom.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                // Call the functionality to collect the book
//                            }
//                        });
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
                                SendRequestDialog sendRequestDialog = new SendRequestDialog();
                                Bundle bundle = new Bundle();
                                bundle.putString("bookid", bookId);
                                bundle.putString("borrowerid", currentUser);
                                sendRequestDialog.setArguments(bundle);
                                sendRequestDialog.show(getSupportFragmentManager(), "Send Request");
                            }
                        });
                        textBorrower.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }

    /**
     * This method is used to open a dialog box in order to send request
     */
    public void openDialog() {
        SendRequestDialog sendRequestDialog = new SendRequestDialog();
        sendRequestDialog.show(getSupportFragmentManager(), "Send Request");
    }

    @Override
    public void onSendRequestConfirm() {
                                Intent toMyRequests = new Intent(BookDescription.this, MyRequestsActivity.class);
                                startActivity(toMyRequests);
    }
}
