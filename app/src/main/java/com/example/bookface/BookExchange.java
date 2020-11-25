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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class handles the activity for Book exchange
 */
public class BookExchange extends AppCompatActivity implements OnMapReadyCallback {

    // Declare variables
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    Geocoder geo, geoStartUp;
    GoogleMap gMap;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser userInstance;
    String owner;
    String author;
    String borrower;
    String title;
    String status;
    String isbn;
    String bookId;
    String imgUrl;
    String currentUser = null;

    // Initialize the constants
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_exchange);

        mFirebaseAuth = FirebaseAuth.getInstance();
        userInstance = mFirebaseAuth.getCurrentUser();

        Button btnTop = findViewById(R.id.myBooksOrMyRequestsButton);
        Button btnBottom = findViewById(R.id.scanBookButton);

        TextView textAuthor = (TextView) findViewById(R.id.authorNameText);
        TextView textTitle = (TextView) findViewById(R.id.titleText);
        TextView textIsbn = (TextView) findViewById(R.id.isbnText);
        TextView textStatus = (TextView) findViewById(R.id.statusText);
        TextView textRequests = (TextView) findViewById(R.id.viewRequestsText);
        ImageView image = (ImageView) findViewById(R.id.imageView);

        // Set the value of the fields in the textViews
        if (userInstance != null){
            currentUser = (String) userInstance.getDisplayName();

            // Retrieve the Book passed through the intent
            Bundle b = getIntent().getExtras();
            System.out.println(b.get("BOOK_ID"));
            if (b!= null) {
                bookId = (String) b.get("BOOK_ID");
            }
            System.out.println("BOOKS ID: "+bookId);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            final DocumentReference docRef = db.collection("books").document(bookId);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error == null && value.exists() && value != null) {
                        owner = value.getString("ownerUsername");
                        author = value.getString("author");
                        isbn = value.getString("isbn");
                        status = value.getString("status");
                        title = value.getString("title");
                        imgUrl = value.getString("imageUrl");

                        if (value.get("borrowerUserName") == null) {
                            borrower = "No current borrower";
                        } else {
                            borrower = value.get("borrowerUserName").toString();
                        }

                        textAuthor.setText(author);
                        textIsbn.setText(isbn);
                        textStatus.setText(status);
                        textTitle.setText(Html.fromHtml("<b>" + title + "</b>"));
                        if(!imgUrl.equals(""))
                            Picasso.with(getApplicationContext()).load(imgUrl).into(image);
                    }

                    // TODO
                    if (owner.equals(currentUser)) {
                        textRequests.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Call Requests activity for this particular book
                            }
                        });

                        btnTop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Call MyBooks Activity
                                Intent toMyBooks = new Intent(BookExchange.this, MyBooks.class);
                                startActivity(toMyBooks);
                            }
                        });

                        btnBottom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Call the functionality to scan the book
                            }
                        });
                    }
                    // Change the options available according to the user logged in
                    else {
//                        Toast.makeText(getApplicationContext(), "Did not enter the area", Toast.LENGTH_SHORT).show();
                        btnTop.setText("My Requests");
                        textRequests.setVisibility(View.INVISIBLE);
                        if (status.equals("borrowed".toLowerCase())) {
                            btnBottom.setText("Scan to Return");
                        }
                        else {
                            btnBottom.setText("Scan to Borrow");
                        }
                        btnTop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Call MyRequests Activity
                            }
                        });

                        btnBottom.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Call to scan the book to return/borrow
                            }
                        });
                    }
                }
            });
        }

        // Get the current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();
    }

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
                    supportMapFragment.getMapAsync(BookExchange.this);
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
            geo = new Geocoder(BookExchange.this, Locale.getDefault());

            gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    try {
                        gMap.clear();
                        if (geo == null) {
                            geo = new Geocoder(BookExchange.this, Locale.getDefault());
                        }
                        List<Address> address = geo.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (address.size() > 0) {
                            gMap.addMarker(new MarkerOptions().position(latLng));
                            String add = address.get(0).getAddressLine(0);
                            textAddress.setText(add);
                        }
                    }
                    catch (IOException ex) {
                        if (ex != null)
                            Toast.makeText(BookExchange.this, "Error:" + ex.getMessage().toString()
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