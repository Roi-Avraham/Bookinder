package com.example.bookinder;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.bookinder.MyProfile.MyProfileActivity;
import com.example.bookinder.Profile.TestActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

public class BookDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_sale);
        getIncomingIntent();
    }

    private void getIncomingIntent() {
        String book_name = getIntent().getStringExtra("book_name");
        String book_image = getIntent().getStringExtra("book_image");
        String author_name = getIntent().getStringExtra("authorName");
        String genre = getIntent().getStringExtra("genre");
        String ownerId = getIntent().getStringExtra("ownerId");
        String ownerImage = getIntent().getStringExtra("ownerImage");
        String ownerName = getIntent().getStringExtra("ownerName");
        String price = getIntent().getStringExtra("price");
        String card_id = getIntent().getStringExtra("card_id");
        String isLiked = getIntent().getStringExtra("isLiked");
        setDetails(book_name, book_image, author_name, genre, ownerImage, ownerName, price);

        TextView name_of_user = findViewById(R.id.name_of_the_user);
        name_of_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CurrentUser.currentUser.equals(ownerId)) {
                    Intent intent = new Intent(BookDetails.this, MyProfileActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(BookDetails.this, TestActivity.class);
                    OtherUser.setOtherUser(ownerId);
                    OtherUser.setOtherName(ownerName);
                    startActivity(intent);
                }
            }
        });
        ImageView heart = findViewById(R.id.heart_icon);
        System.out.println("is like is: " + isLiked);
        if (isLiked.equals("True")) {
            heart.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                heart.setImageResource(R.drawable.ic_baseline_favorite_24);
                if (isLiked.equals("False")) {
                    JSONObject liked = new JSONObject();
                    try {
                        liked.put("current_user", CurrentUser.currentUser);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), liked.toString());

                    postRequest(ServerAddress.serverAddress + "/like/" + CurrentUser.currentUser + "/" + card_id, body);
                }
            }
        });
        ImageView go_back = findViewById(R.id.goBack);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void setDetails(String book_name, String book_image, String author_name, String genre,
                            String ownerImage, String ownerName, String price) {
        TextView nameOfBook = findViewById(R.id.name_of_the_book);
        nameOfBook.setText(book_name);

        TextView authorName = findViewById(R.id.name_of_the_writer);
        String s1 = "Writer: " + author_name;
        authorName.setText(s1);
        String s2 = "Genre: " + genre;
        TextView genreName = findViewById(R.id.genre);
        genreName.setText(s2);

        TextView ownerNameView = findViewById(R.id.name_of_the_user);
        ownerNameView.setText(ownerName);

        TextView priceView = findViewById(R.id.price);
        String s3 = "Price: " + price +" $";
        priceView.setText(s3);

        ImageView imageView1 = findViewById(R.id.bookImage);
        ImageView imageView2 = findViewById(R.id.user_image);

        Picasso.get().load(book_image).into(imageView1);
        Picasso.get().load(ownerImage).into(imageView2);
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
