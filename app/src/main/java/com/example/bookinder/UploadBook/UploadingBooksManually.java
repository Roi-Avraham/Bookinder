package com.example.bookinder.UploadBook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookinder.MainActivity;
import com.example.bookinder.ProfileExpansion.ProfileExpansion;
import com.example.bookinder.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class UploadingBooksManually extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_manually);

        Button done = findViewById(R.id.doneBtn);
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(UploadingBooksManually.this, ProfileExpansion.class);
                UploadingBooksManually.this.startActivity(intent);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(UploadingBooksManually.this, AddBookLoveActivity.class);
                UploadingBooksManually.this.startActivity(intent);
            }
        });


    }
}
