package com.example.bookface;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserProfile extends AppCompatActivity {

    private BottomNavigationView navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        navBar = findViewById(R.id.nav_bar);

        navBar.setOnNavigationItemSelectedListener(navBarMethod);
    }

    private  BottomNavigationView.OnNavigationItemSelectedListener navBarMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


            switch (menuItem.getItemId()){
                case R.id.my_books:
                    Intent toMyBooks = new Intent(UserProfile.this, SignupActivity.class);
                    startActivity(toMyBooks);
                case R.id.requests:
                    Intent toRequests = new Intent(UserProfile.this, SignupActivity.class);
                    startActivity(toRequests);
                case R.id.search:
                    Intent toSearch = new Intent(UserProfile.this, SignupActivity.class);
                    startActivity(toSearch);
                case R.id.notification:
                    Intent toNotification = new Intent(UserProfile.this, SignupActivity.class);
                    startActivity(toNotification);
            }
            return false;
        }
    };
}
