package com.example.bookinder.ProfileExpansion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.bookinder.Login.LoginActivity;
import com.example.bookinder.MainActivity;
import com.example.bookinder.R;
import com.example.bookinder.Register.RegisterActivity;
import com.example.bookinder.UploadBook.UploadingBooksManually;

public class ProfileExpansion extends AppCompatActivity {
    private final int GALLERY_REQ_CODE = 1000;
    ImageView profilePicture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_expansion);

        profilePicture = findViewById(R.id.profileImage);
        Button uploadProfile = findViewById(R.id.upload);
        Button enterManually = findViewById(R.id.enterManually);
        Button done = findViewById(R.id.doneBtn);

        uploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        enterManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(ProfileExpansion.this, UploadingBooksManually.class);
                ProfileExpansion.this.startActivity(intent);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(ProfileExpansion.this, MainActivity.class);
                ProfileExpansion.this.startActivity(intent);
            }
        });
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == RESULT_OK) {
            if(resultCode ==GALLERY_REQ_CODE) {
                profilePicture.setImageURI(data.getData());
            }
        }
    }
}