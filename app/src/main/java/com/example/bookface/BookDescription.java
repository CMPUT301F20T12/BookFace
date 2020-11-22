package com.example.bookface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
// import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
// import android.widget.ScrollView;
import android.widget.TextView;
// import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// import org.w3c.dom.Text;

public class BookDescription extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    FirebaseUser userInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);

        // Retrieve the objects passed into the intent object
        Bundle data = getIntent().getExtras();
        Book book = data.getParcelable("book");

        // TODO
        // Declare the fireAuth variable to get the currentUser()
//        userInstance = mFirebaseAuth.getCurrentUser();
//        String currentUserName = userInstance.getDisplayName();

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

        // TODO
////        if (book.getOwnerUsername() != currentUserName) { // If the user is not the owner  (called from search page)
            btnTop.setText("Search");
            btnEdit.setVisibility(View.INVISIBLE);
            btnBottom.setText("Send Request");

            btnTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call Search Books Activity
                    Intent intent = new Intent(BookDescription.this, SearchActivity.class);
                    startActivity(intent);
                }
            });

            btnBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call Send Request Activity

                }
            });
            textBorrower.setVisibility(View.INVISIBLE);
//        }
//        else {
//            btnEdit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Call Add/Edit Book activity
//                }
//            });
//
//            btnTop.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Call MyBooks Activity
//                }
//            });
//
//            btnBottom.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Call the functionality to collect the book
//                }
//            });
//            textBorrower.setText(book.getBorrowerUsername().toString());
//        }

        textAuthor.setText(book.getAuthor());
        textDescHeading.setText(Html.fromHtml("<b>DESCRIPTION:</b>"));
        textIsbn.setText(book.getISBN());
        textDescription.setText(book.getDescription());
        textStatus.setText(book.getStatus());
        textTitle.setText(Html.fromHtml("<b>" + book.getTitle().toString()+"</b>"));

        // TODO
//        Use android:background="@drawable/<ImageFileName>" in the relative layout in xml file.
//        Only catch is that I need to put the image file in drawable folder in app/res/drawable in order to display it


    }

//    public void clickImage(View view) {
//        Intent showImage = new Intent(BookDescription.this, AddImage.class);
//        startActivity(showImage);
//    }
}