package com.example.bookface;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        Bundle b = getIntent().getExtras();
        String imgUrl = (String) b.get("imgURL");

        ImageView image = (ImageView) findViewById(R.id.enlarged_image);
        // Set the image enlarged image
        Picasso.with(getApplicationContext()).load(imgUrl).into(image);

        // Return to the previous activity when clicked again
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}