package com.example.bookface;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class AddEditBookActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView scan;
    private TextView back;
    private EditText isbn;
    private EditText author;
    private EditText title;
    private EditText description;
    private Button confirm;
    private FloatingActionButton cameraButton;
    private FloatingActionButton galleryButton;
    private ImageView imageView;

    static final int REQUEST_IMAGE_CAPTURE = 101;
    static final int RESULT_LOAD_IMAGE = 1;

    private Book book;
    String localIsbn, localAuthors, localDescription, localTitle, localImage, localUsername;

    FirebaseAuth mFirebaseAuth;

    private RequestQueue mRequestQueue;
    private static  final  String BASE_URL="https://www.googleapis.com/books/v1/volumes?q=";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_book);

        scan = findViewById(R.id.scanBookButton);
        back = findViewById(R.id.backAddEditBookButton);
        isbn = findViewById(R.id.editISBN);
        author = findViewById(R.id.editName);
        title = findViewById(R.id.editTitle);
        description = findViewById(R.id.editDescription);
        confirm = findViewById(R.id.addEditBookConfirm);
        cameraButton = findViewById(R.id.cameraImage);
        galleryButton = findViewById(R.id.galleryImage);
        imageView = findViewById(R.id.addEditImageView);
        mRequestQueue = Volley.newRequestQueue(this);
        mFirebaseAuth = FirebaseAuth.getInstance();

        scan.setOnClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toMyBooks = new Intent(AddEditBookActivity.this, MyBooks.class);
                startActivity(toMyBooks);
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                    Toast.makeText(getApplicationContext(), "Camera is unavailable", Toast.LENGTH_LONG);
                }
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        Bundle b = getIntent().getExtras();
        if (b != null) {
            String isbnNumber = (String) b.get("Book");
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            final DocumentReference docRef = db.collection("books").document(isbnNumber);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null && value.exists() && value != null) {
//                        owner = value.getString("ownerUsername");
//                        borrower = value.getString("borrowerUsername");
                        author.setText(value.getString("author"));
                        description.setText(value.getString("description"));
//                        status.value.getString("status"));
                        title.setText(value.getString("title"));
                        isbn.setText(isbnNumber);
                        String imgUrl = value.getString("imageUrl");

                        Picasso.with(AddEditBookActivity.this).load(imgUrl).into(imageView);
                    }
                }
            });
        }

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                this.writeDB();
            }

            private void writeDB() {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                if(isbn.getText().toString().length()==0)
                    isbn.setError("FIELD CANNOT BE EMPTY");
                else if(author.getText().toString().length()==0)
                    author.setError("FIELD CANNOT BE EMPTY");
                else if(title.getText().toString().length()==0)
                    title.setError("FIELD CANNOT BE EMPTY");
                else if(imageView.getDrawable() == null)
                    Toast.makeText(AddEditBookActivity.this, "Image not attached!", Toast.LENGTH_SHORT).show();
                else{
                    localIsbn  =isbn.getText().toString();
                    localAuthors = author.getText().toString();
                    localTitle = title.getText().toString();
                    localDescription = description.getText().toString();

                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    StorageReference storageRef = storage.getReferenceFromUrl("gs://bookface-cmput301f20t12.appspot.com");
                    StorageReference mountainsRef = storageRef.child(localIsbn);
                    StorageReference mountainImagesRef = storageRef.child("images/"+localIsbn);

                    mountainsRef.getName().equals(mountainImagesRef.getName());    // true
                    mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false

                    imageView.setDrawingCacheEnabled(true);
                    imageView.buildDrawingCache();
                    Bitmap bitmap = imageView.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data1 = baos.toByteArray();

                    UploadTask uploadTask = mountainsRef.putBytes(data1);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                            {
                                @Override
                                public void onSuccess(Uri downloadUrl)
                                {
                                    localImage = downloadUrl.toString();

                                    mFirebaseAuth = FirebaseAuth.getInstance();
                                    FirebaseUser userInstance = mFirebaseAuth.getCurrentUser();

                                    localUsername = userInstance.getDisplayName();

                                    book = new Book(localTitle, localAuthors, localIsbn, localDescription, "Available", localUsername, "Null", localImage);

                                    db.collection("books")
//                                            .document(book.getISBN()+localUsername).set(book).addOnSuccessListener(new OnSuccessListener<Void>()
                                            .document(book.getISBN()).set(book).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            String docPath = "users/".concat(localUsername);
                                            DocumentReference docRef = db.document(docPath);

                                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists()) {
                                                            Map userData = document.getData();
                                                            System.out.println("DOCUMENT EXISTS!");
                                                            if(userData != null){

                                                                final String TAG = "Completeion Message" ;
                                                                ArrayList<String> myBookList = (ArrayList<String>)document.get("booksOwned");
                                                                myBookList.add(localIsbn);
                                                                System.out.println(myBookList);

                                                                docRef.update("booksOwned", myBookList)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {

                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                                                Toast.makeText(AddEditBookActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                                                                                Intent toMyBooks = new Intent(AddEditBookActivity.this, MyBooks.class);
                                                                                startActivity(toMyBooks);
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.w(TAG, "Error updating document", e);
                                                                            }
                                                                        });


//                                                                db.document(docPath+"/booksOwned").set(myBookList);
                                                            }
                                                        }
                                                        else{
                                                            System.out.println("DOC does not exist");
                                                        }
                                                    }
                                                }
                                            });





                                        }
                                    })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(AddEditBookActivity.this, "Error Adding Book", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }});
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Scan scanObj = new Scan(AddEditBookActivity.this);
        scanObj.scanCode();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String code = result.getContents();
            if (code != null) {
                this.extractContents(code);
            }
        }

        else if(isbn.getText().toString().length() == 0)
            Toast.makeText(AddEditBookActivity.this, "Scan Book First!", Toast.LENGTH_SHORT).show();

        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }

        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri imageUri = data.getData();
            Bitmap image = null;
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageView.setImageBitmap(image);
        }
    }

    public void parseJson(String key, String code) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, key.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("items");
                            int flag = 0;
                            for (int i = 0; i < items.length(); i++) {
                                JSONObject item = items.getJSONObject(i);
                                JSONObject volumeInfo = item.getJSONObject("volumeInfo");
                                JSONArray identifiers = volumeInfo.getJSONArray("industryIdentifiers");
                                for (int j = 0; j < identifiers.length(); j++) {
                                    try {
                                        String identifier = identifiers.getJSONObject(j).getString("identifier");
                                        if (code.equals(identifier)) {
                                            flag = 1;
                                            break;
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                                if (flag == 1) {
                                    isbn.setText(code);
                                    title.setText(volumeInfo.getString("title"));
                                    description.setText(volumeInfo.getString("description"));
                                    JSONArray authors = volumeInfo.getJSONArray("authors");
                                    String authorsStr = "";
                                    for (int j = 0; j < authors.length(); j++) {
                                        if(j!=0)
                                            authorsStr = authorsStr + ", ";
                                        authorsStr = authorsStr + authors.getString(j);
                                    }
                                    author.setText(authorsStr);
                                    break;
                                }
                            }
                            if(flag ==0){
                                Toast.makeText(AddEditBookActivity.this, "Book Not Found, Add Manually!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(AddEditBookActivity.this, "Book Not Found, Add Manually!", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                            Log.e("TAG" , e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mRequestQueue.add(request);
    }

    public void extractContents(String code) {
        Uri uri=Uri.parse(BASE_URL+code);
        Uri.Builder buider = uri.buildUpon();
        this.parseJson(buider.toString(), code);
    }

}
