package com.example.bookinder.UploadBook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookinder.CurrentUser;
import com.example.bookinder.R;
import com.example.bookinder.ServerAddress;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ScanShelfActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_a_shelf);

        Intent intent = getIntent();
        String current_user = CurrentUser.currentUser;
        if (intent.hasExtra("scanned_image")) {
            String scanned_image = intent.getStringExtra("scanned_image");
            Uri uri = Uri.parse(scanned_image);
            String str = uri.toString();
            URL url = null;
            try {
                url = new URL(str);
                scanned_image = url.toString();
                sendScan(current_user,scanned_image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }




    }
    public void sendScan(String current_user, String scanned_image) {
        JSONObject profileImageForm = new JSONObject();
        try {
            profileImageForm.put("current_user", current_user);
            profileImageForm.put("profile_image", scanned_image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), profileImageForm.toString());

        postRequest(ServerAddress.serverAddress, body);
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
