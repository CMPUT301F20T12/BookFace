package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.Map;

public class BookDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_description);

        String bookISBN = getIntent().getStringExtra("BOOK_ISBN");


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
//        TextView textDescHeading = findViewById(R.id.descriptionHeadingText);
        TextView textDescription = findViewById(R.id.bookDescriptionText);
        TextView textBorrower = findViewById(R.id.borrowerNameText);
        ImageView image = (ImageView) findViewById(R.id.imageView);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("books").document(bookISBN);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map bookData = document.getData();
                        String author = bookData.get("author").toString();
                        String title = bookData.get("title").toString();
                        String status = bookData.get("status").toString();
                        String decs = bookData.get("description").toString();

                        String borrower = "";
                        if (bookData.get("borrowerUserName") == null) {
                            borrower = "No current borrower";
                        } else {
                            borrower = bookData.get("borrowerUserName").toString();
                        }

                        if (bookData.get("imageUrl") != null) {
                            String imageUrl = bookData.get("imageUrl").toString();
                            new DownloadImageTask(image).execute(imageUrl);
                        }

                        textAuthor.setText(author);
                        textIsbn.setText(bookISBN);
                        textTitle.setText(title);
                        textStatus.setText(status);
                        textDescription.setText(decs);
                        textBorrower.setText(borrower);


                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


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


//        Use android:background="@drawable/<ImageFileName>" in the relative layout in xml file.
//        Only catch is that I need to put the image file in drawable folder in app/res/drawable in order to display it


    }


//    public void clickImage(View view) {
//        Intent showImage = new Intent(BookDescription.this, AddImage.class);
//        startActivity(showImage);
//    }



//----------------------------------------------------------------------------------
// code used from the following url:
// https://medium.com/@crossphd/android-image-loading-from-a-string-url-6c8290b82c5e
//----------------------------------------------------------------------------------

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bmp = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bmp;
    }
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
}