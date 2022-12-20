package com.example.bookinder.UploadBook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookinder.ProfileExpansion.ProfileExpansion;
import com.example.bookinder.R;
import com.example.bookinder.Register.RegisterActivity;

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

public class AddBookLoveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book_love);

        Button add = findViewById(R.id.adding_btn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(view);
                Intent intent = getIntent();
                String current_user = intent.getStringExtra("current_user");
                intent = new  Intent(AddBookLoveActivity.this, UploadingBooksManually.class);
                intent.putExtra("current_user",current_user);
                AddBookLoveActivity.this.startActivity(intent);
            }
        });
    }
    public void add(View view) {
        Intent intent = getIntent();
        String current_user = intent.getStringExtra("current_user");
        EditText name_of_bookV = findViewById(R.id.name_of_the_book);
        EditText name_of_writerV = findViewById(R.id.writer);
        EditText genreV = findViewById(R.id.genre);
        EditText rateV = findViewById(R.id.rate);

        String name_of_book = name_of_bookV.getText().toString().trim();
        String name_of_writer = name_of_writerV.getText().toString().trim();
        String genre = genreV.getText().toString().trim();
        String rate = rateV.getText().toString().trim();


        JSONObject addingForm = new JSONObject();
        try {
            addingForm.put("current_user", current_user);
            addingForm.put("name_of_book", name_of_book);
            addingForm.put("name_of_writer", name_of_writer);
            addingForm.put("genre", genre);
            addingForm.put("rate", rate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), addingForm.toString());

        postRequest("http://192.168.1.169:5000/addbookmanually", body);
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
                    final String responseString = response.body().string().trim();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responseString.equals("success")) {
                                finish();
                            } else {
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
