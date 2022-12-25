package com.example.bookinder;//package com.example.bookinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bookinder.MyProfile.MyProfileActivity;
import com.example.bookinder.MyProfile.ui.Profile.ProfileFragment;
import com.example.bookinder.Profile.Profile_activity;
import com.example.bookinder.ProfileExpansion.ProfileExpansion;
import com.example.bookinder.UploadBook.UploadingBooksManually;
import com.example.bookinder.UploadBook.UplodingBook;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.button_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu);
    }
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_cart:
                intent = new  Intent(MainActivity.this, UplodingBook.class);
                MainActivity.this.startActivity(intent);
                return true;
            case R.id.nav_person:
                intent = new  Intent(MainActivity.this, MyProfileActivity.class);
                MainActivity.this.startActivity(intent);
                return true;
        }
        return false;
   }
}
