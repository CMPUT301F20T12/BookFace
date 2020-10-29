package com.example.bookface;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BookDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);

        // Retrieve the objects passed into the intent object
        // TODO
        // Declare the fireAuth variable to get the currentUser()

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

        // TODO
//        if (book.owner != currentUser) {
//            btnTop.setText("Search");
//            btnEdit.setVisibility(View.INVISIBLE);
//            btnBottom.setText("Send Request");
//            btnTop.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Call Search Books Activity
//                }
//            });
//
//            btnBottom.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    // Call Send Request Activity
//                }
//            });
//            textBorrower.setVisibility(View.INVISIBLE);
//        }
//        else {
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call Add/Edit Book activity
                }
            });

            btnTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call MyBooks Activity
                }
            });

            btnBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Call the functionality to collect the book
                }
            });
//            textBorrower.setText(book.getBorrowerUsername().toString());
//        }

        // TODO
//        textAuthor.setText(book.getAuthor().toString());
//        textDescHeading.setText(Html.fromHtml("<b>DESCRIPTION:</b>"));
//        textIsbn.setText(book.getIsbn().toString());
//        textDescription.setText(book.getDescription().toString());
//        textStatus.setText(book.getStatus.toString());
//        textTitle.setText(Html.fromHtml("<b>" + book.getTitle().toString()+"</b>"));
    }
}