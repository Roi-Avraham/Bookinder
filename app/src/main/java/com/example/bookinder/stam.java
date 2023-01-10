package com.example.bookinder;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class stam extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_sending_image);
        ImageView imageView = findViewById(R.id.bookImage);
        Glide.with(this).load("https://img.lovepik.com/element/40103/5278.png_300.png").into(imageView);
    }
}
