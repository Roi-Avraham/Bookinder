package com.example.bookinder.ProfileExpansion;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bookinder.CurrentUser;
import com.example.bookinder.Login.LoginActivity;
import com.example.bookinder.MainActivity;
import com.example.bookinder.R;
import com.example.bookinder.Register.RegisterActivity;
import com.example.bookinder.ServerAddress;
import com.example.bookinder.UploadBook.ScanShelfActivity;
import com.example.bookinder.UploadBook.UploadingBooksManually;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileExpansion extends AppCompatActivity {
    ImageView profilePicture;
    ActivityResultLauncher <String> mTakePhoto;
    ActivityResultLauncher <String> scanTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_expansion);

        CheckBox checkBox1 = findViewById(R.id.checkBox1);
        CheckBox checkBox2 = findViewById(R.id.checkBox2);
        CheckBox checkBox3 = findViewById(R.id.checkBox3);
        CheckBox checkBox4 = findViewById(R.id.checkBox4);
        CheckBox checkBox5 = findViewById(R.id.checkBox5);
        CheckBox checkBox6 = findViewById(R.id.checkBox6);

        String current_user = CurrentUser.currentUser;

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
                        if(result!=null) {
                            profilePicture.setImageURI(result);
                            ContentResolver contentResolver = getContentResolver();
                            try {
                                InputStream inputStream = contentResolver.openInputStream(result);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                byte[] profile_image =convertBitMapToString(bitmap);
                                sendProfileImage(profile_image,current_user);
                            } catch (Exception e){
                            }
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
                        //intent.putExtra("current_user", current_user);
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
                ProfileExpansion.this.startActivity(intent);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("checkbox_1", checkBox1.isChecked());
                editor.putBoolean("checkbox_2", checkBox2.isChecked());
                editor.putBoolean("checkbox_3", checkBox3.isChecked());
                editor.putBoolean("checkbox_4", checkBox4.isChecked());
                editor.putBoolean("checkbox_5", checkBox5.isChecked());
                editor.putBoolean("checkbox_6", checkBox6.isChecked());
                editor.apply();

                // to retrieve the values
                sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
                boolean isChecked1 = sharedPreferences.getBoolean("checkbox_1", false);  // default value is false
                boolean isChecked2 = sharedPreferences.getBoolean("checkbox_2", false);  // default value is false
                boolean isChecked3 = sharedPreferences.getBoolean("checkbox_3", false);  // default value is false
                boolean isChecked4 = sharedPreferences.getBoolean("checkbox_4", false);  // default value is false
                boolean isChecked5 = sharedPreferences.getBoolean("checkbox_5", false);  // default value is false
                boolean isChecked6 = sharedPreferences.getBoolean("checkbox_6", false);  // default value is false

                String genres  = "";
                if(isChecked1) {
                    genres = genres + "Novel,";
                }
                if(isChecked2) {
                    genres = genres + "History,";
                }
                if(isChecked3) {
                    genres = genres + "Thriller,";
                }
                if(isChecked4) {
                    genres = genres + "Science,";
                }
                if(isChecked5) {
                    genres = genres + "Fantasy,";
                }
                if(isChecked6) {
                    genres = genres + "Children,";
                }
                sendGenres(current_user,genres);
                Intent intent = new  Intent(ProfileExpansion.this, MainActivity.class);
                ProfileExpansion.this.startActivity(intent);
            }
        });

        scan_a_shelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanTakePhoto.launch("image/*");
                Intent intent = new  Intent(ProfileExpansion.this, MainActivity.class);
                ProfileExpansion.this.startActivity(intent);
            }
        });
    }
    public void sendProfileImage(byte[] profile_image, String current_user) {
        JSONObject profileImageForm = new JSONObject();
        try {
            profileImageForm.put("current_user", current_user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("current_user",profileImageForm.toString())
                .addFormDataPart("image","image.png",
                        RequestBody.create(MediaType.parse("image/png"),profile_image)).build();

        postRequest(ServerAddress.serverAddress + "/update_profile_pic/" + current_user, requestBody);
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

    public void sendGenres(String current_user, String genres) {
        JSONObject genresForm = new JSONObject();
        try {
            genresForm.put("current_user", current_user);
            genresForm.put("genres", genres);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), genresForm.toString());

        postRequest(ServerAddress.serverAddress + "/genres/" + current_user, body);
    }
    public static byte[] convertBitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] data = outputStream.toByteArray();
        return data;
    }
}