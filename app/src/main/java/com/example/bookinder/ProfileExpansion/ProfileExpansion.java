package com.example.bookinder.ProfileExpansion;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookinder.Login.LoginActivity;
import com.example.bookinder.MainActivity;
import com.example.bookinder.R;
import com.example.bookinder.Register.RegisterActivity;
import com.example.bookinder.UploadBook.ScanShelfActivity;
import com.example.bookinder.UploadBook.UploadingBooksManually;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileExpansion extends AppCompatActivity {
    private final int GALLERY_REQ_CODE = 1000;
    ImageView profilePicture;
    ActivityResultLauncher <String> mTakePhoto;
    ActivityResultLauncher <String> scanTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_expansion);

        Intent intent = getIntent();
        String current_user = intent.getStringExtra("current_user");


        profilePicture = findViewById(R.id.profileImage);
        Button uploadProfile = findViewById(R.id.upload);
        Button enterManually = findViewById(R.id.enterManually);
        Button done = findViewById(R.id.doneBtn);
        Button scan_a_shelf = findViewById(R.id.scan_a_shelf);

        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        profilePicture.setImageURI(result);
                        if(result!=null) {
                            sendProfileImage(result,current_user);
                        }
                    }
                }
        );
        scanTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        Intent intent = new  Intent(ProfileExpansion.this, ScanShelfActivity.class);
                        intent.putExtra("current_user", current_user);
                        intent.putExtra("scanned_image", result.toString());
                        ProfileExpansion.this.startActivity(intent);
                    }
                }
        );

        uploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTakePhoto.launch("image/*");
            }
        });

        enterManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(ProfileExpansion.this, UploadingBooksManually.class);
                intent.putExtra("current_user", current_user);
                ProfileExpansion.this.startActivity(intent);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(ProfileExpansion.this, MainActivity.class);
                intent.putExtra("current_user", current_user);
                ProfileExpansion.this.startActivity(intent);
            }
        });

        scan_a_shelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanTakePhoto.launch("image/*");
            }
        });
    }
    public void sendProfileImage(Uri result, String current_user) {
        JSONObject profileImageForm = new JSONObject();
        try {
            profileImageForm.put("current_user", current_user);
            profileImageForm.put("profile_image", result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), profileImageForm.toString());

        postRequest("http://192.168.1.169:5000/getProfileImage", body);
    }
    public void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.d("FAIL", e.getMessage());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}