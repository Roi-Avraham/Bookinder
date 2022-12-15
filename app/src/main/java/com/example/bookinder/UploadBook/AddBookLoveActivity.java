package com.example.bookinder.UploadBook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookinder.R;

public class AddBookLoveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_love);

        Button add = findViewById(R.id.adding_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(AddBookLoveActivity.this, UploadingBooksManually.class);
                AddBookLoveActivity.this.startActivity(intent);
            }
        });
    }
}
