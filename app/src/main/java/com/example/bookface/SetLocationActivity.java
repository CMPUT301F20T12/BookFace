package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class handles the activity for Book exchange
 */
public class SetLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
    // Declare variables
    private Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Geocoder geo, geoStartUp;
    private GoogleMap gMap;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser userInstance;
    private String owner, author, borrower, title, status, isbn, imgUrl;
    private String requestId, bookId;
    private String currentUser = null;
//    private DocumentReference docRefRequest;
    FirebaseFirestore db;

    // Initialize the constants
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String TAG = "SetLocationTag";

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userInstance = mFirebaseAuth.getCurrentUser();

        Button btnTop = findViewById(R.id.myBooksOrMyRequestsButton);
        Button btnBottom = findViewById(R.id.confirmButton);

        TextView textAuthor = (TextView) findViewById(R.id.authorNameText);
        TextView textTitle = (TextView) findViewById(R.id.titleText);
        TextView textIsbn = (TextView) findViewById(R.id.isbnText);
        TextView textStatus = (TextView) findViewById(R.id.statusText);
        ImageView image = (ImageView) findViewById(R.id.imageView);

        // Set the value of the fields in the textViews
        if (userInstance != null){
            currentUser = (String) userInstance.getDisplayName();

            // Retrieve the request passed through the intent
            Bundle b = getIntent().getExtras();
//            System.out.println(b.get("BOOK_ID"));
            if (b!= null) {
//                bookId = (String) b.get("BOOK_ID");
                requestId = (String) b.get("REQUEST_ID");
                System.out.println("REQ ID: "+ requestId);
            }
            db = FirebaseFirestore.getInstance();

            // Retrieve the book from firebase
            DocumentReference docRefRequest = db.collection("requests").document(requestId);
            docRefRequest.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null && value.exists() && value != null) {
                        docRefRequest.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map requestData = document.getData();
                                        DocumentReference bookRef = (DocumentReference) requestData.get("bookid");
                                        bookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot value = task.getResult();
                                                    if (value.exists()) {
//
                                                        Map bookData = value.getData();
                                                        owner = bookData.get("ownerUsername").toString();
                                                        author = bookData.get("author").toString();
                                                        isbn = value.get("isbn").toString();
                                                        status = value.get("status").toString();
                                                        title = value.get("title").toString();
                                                        imgUrl = value.get("imageUrl").toString();
                                                        bookId = isbn.concat(owner);
                                                        System.out.println("Owner: "+owner+" ++++++++++++++++");
                                                        System.out.println("ISBN: "+isbn+" ++++++++++++++++");

                                                        if (value.get("borrowerUserName") == null) {
                                                            borrower = "No current borrower";
                                                        }
                                                        else {
                                                            borrower = value.get("borrowerUserName").toString();
                                                        }

                                                        textAuthor.setText(author);
                                                        textIsbn.setText(isbn);
                                                        textStatus.setText(status);
                                                        textTitle.setText(Html.fromHtml("<b>" + title + "</b>"));
                                                        if (!imgUrl.equals("")) {
                                                            Picasso.with(getApplicationContext()).load(imgUrl).into(image);
                                                        }
                    //
                    //                                // TODO
                                                    if (owner.equals(currentUser)) {
                                                        // Get the current location
                                                        fusedLocationProviderClient =
                                                                LocationServices.getFusedLocationProviderClient(SetLocationActivity.this);
                                                        fetchLastLocation();



                                                        btnTop.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                // Call MyBooks Activity
                                                                Intent toMyBooks = new Intent(SetLocationActivity.this, MyBooks.class);
                                                                startActivity(toMyBooks);
                                                            }
                                                        });

                                                        btnBottom.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                // Set the location
                                                                LocationHelper location = new LocationHelper(
                                                                        currentLocation.getLatitude(), currentLocation.getLongitude());

                                                                DocumentReference requestRef = db.collection("requests").document(requestId);

                                                                requestRef
                                                                        .update("location", location)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                Toast.makeText(SetLocationActivity.this, "Location saved.", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(SetLocationActivity.this, "Location was not saved.", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });

                                                                // Call the functionality to intent to display the book exchange
                                                                Intent toBookDescription = new Intent(SetLocationActivity.this,
                                                                        BookDescription.class);
                                                                toBookDescription.putExtra("BOOK_ID", bookId);
                                                                startActivity(toBookDescription);
                                                            }
                                                        });
                                                    }

                                                    } else {
                                                        Log.d(TAG, "No such document");
                                                    }
                                                } else {
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });

                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    /**
     * This method fetches the current location of the device
     * This code was built up upon the video: https://www.youtube.com/watch?v=boyyLhXAZAQ&feature=youtu.be
     */
    private void fetchLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + " " + currentLocation.getLongitude(),
                            Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment)
                            getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync((OnMapReadyCallback) SetLocationActivity.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        TextView textAddress = (TextView) findViewById(R.id.addressText);

        // Retrieve current location and put marker
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        gMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));

        List<Address> addresses = null;
        geoStartUp = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geoStartUp.getFromLocation(latLng.latitude, latLng.longitude, 1);
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            System.out.println(addresses);
            String address = addresses.get(0).getAddressLine(0);
            textAddress.setText(address);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            gMap.addMarker(new MarkerOptions().position(latLng));
        }

        // Get the new location
        if (gMap != null) {
            geo = new Geocoder(SetLocationActivity.this, Locale.getDefault());

            gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    try {
                        gMap.clear();
                        if (geo == null) {
                            geo = new Geocoder(SetLocationActivity.this, Locale.getDefault());
                        }
                        List<Address> address = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (address.size() > 0) {
                            gMap.addMarker(new MarkerOptions().position(latLng));
                            String add = address.get(0).getAddressLine(0);
                            textAddress.setText(add);
//                            docRefRequest.update("address",add);
                        }
                    }
                    catch (IOException ex) {
                        if (ex != null)
                            Toast.makeText(SetLocationActivity.this, "Error:" + ex.getMessage().toString()
                                    , Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLastLocation();
                }
                break;
        }
    }
}