package com.example.bookface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class AddImage extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 101;
    static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        FloatingActionButton camera = findViewById(R.id.cameraFloatingBtn);
        FloatingActionButton gallery = findViewById(R.id.galleryFloatingBtn);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add camera functionality
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                    Toast.makeText(getApplicationContext(), "Camera is unavailable", Toast.LENGTH_LONG);
                }

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                    Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView img = (ImageView) findViewById(R.id.viewImage);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);

            // TODO
            // Send this Image to the firebase
        }

        else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            img.setImageURI(selectedImage);

            // TODO
            // Send this Image to the firebase
        }

        Toast.makeText(getApplicationContext(), "Image added successfully", Toast.LENGTH_LONG).show();
    }

    public void goBack(View view) {
        finish();
    }
}