package com.example.bookinder.UploadBook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookinder.CurrentUser;
import com.example.bookinder.MainActivity;
import com.example.bookinder.ProfileExpansion.ProfileExpansion;
import com.example.bookinder.R;
import com.example.bookinder.ServerAddress;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadingBooksManually extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_manually);

        String current_user = CurrentUser.currentUser;

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

        JSONObject UploadingForm = new JSONObject();
        try {
            UploadingForm.put("current_user", current_user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), UploadingForm.toString());

        postRequest(ServerAddress.serverAddress + "/getBooksYouEntered", body);
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
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    System.out.println(jsonObject);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!jsonObject.equals("{}")) {
                                try {
                                    int number_of_books =  Integer.parseInt(jsonObject.getString("number"));

                                    for (int i = 0; i< number_of_books; i++) {
                                        TableLayout table = findViewById(R.id.table);
                                        TableRow row = (TableRow) LayoutInflater.from(UploadingBooksManually.this).inflate(R.layout.recycle_row, null);
                                        ((TextView)row.findViewById(R.id.name_of_the_book)).setText(jsonObject.getString("name_of_book"+ Integer.toString(i)));
                                        ((TextView)row.findViewById(R.id.name_of_writer)).setText(jsonObject.getString("name_of_writer"+ Integer.toString(i)));
                                        ((TextView)row.findViewById(R.id.genre)).setText(jsonObject.getString("genre"+ Integer.toString(i)));
                                        ((TextView)row.findViewById(R.id.rate)).setText(jsonObject.getString("rate"+ Integer.toString(i)));
                                        table.addView(row);
                                        table.requestLayout();
                                    }

                                } catch (JSONException e) {
                                }
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
