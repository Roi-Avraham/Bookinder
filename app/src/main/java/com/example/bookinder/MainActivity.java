package com.example.bookinder;//package com.example.bookinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.bookinder.MyProfile.MyProfileActivity;
import com.example.bookinder.MyProfile.ui.BooksForSale.ItemBookAdapter;
import com.example.bookinder.MyProfile.ui.Profile.ProfileFragment;
import com.example.bookinder.Profile.Profile_activity;
import com.example.bookinder.ProfileExpansion.ProfileExpansion;
import com.example.bookinder.UploadBook.UploadingBooksManually;
import com.example.bookinder.UploadBook.UplodingBook;
import com.example.bookinder.models.ItemBookData;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    ArrayList<ItemBookData> itemBooksData = new ArrayList<ItemBookData>();
    RecyclerView recyclerView;
    View view;
    ItemBookAdapter itemBookAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.button_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.menu);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        view = findViewById(android.R.id.content);

        JSONObject bookForSaleForm = new JSONObject();
        try {
            bookForSaleForm.put("current_user",  CurrentUser.getCurrentUser());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bookForSaleForm.toString());

        postRequest(ServerAddress.serverAddress+"/home/" +  CurrentUser.getCurrentUser(), body);

    }
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
//        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
//        return true;
//    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_cart:
                intent = new  Intent(MainActivity.this, UplodingBook.class);
                MainActivity.this.startActivity(intent);
                return true;
            case R.id.nav_person:
                intent = new  Intent(MainActivity.this, MyProfileActivity.class);
                MainActivity.this.startActivity(intent);
                return true;
        }
        return false;
   }


    public void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

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
            }

            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    final String responseString = response.body().string().trim();
                    System.out.println("res is: " + responseString);
                    Gson gson = new Gson();
                    String[] all_cards = gson.fromJson(responseString, String[].class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i = 0; i < all_cards.length; i++) {
                                JSONObject bookForSaleForm = new JSONObject();
                                try {
                                    bookForSaleForm.put("card_id", all_cards[i]);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bookForSaleForm.toString());

                                postRequestCard(ServerAddress.serverAddress+"/items/" + all_cards[i] + "/" + CurrentUser.currentUser, body);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void postRequestCard(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .build();

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
            }
            @Override
            public void onResponse(Call call, final Response response) {
                try {
                    final String responseString = response.body().string().trim();
                    JSONObject jsonObject = new JSONObject(responseString);
                    String title = jsonObject.getString("title");
                    String method = jsonObject.getString("method");
                    String book_image = jsonObject.getString("book_image");
                    System.out.println("the image is: " + book_image);
                    String author = jsonObject.getString("author");
                    String price = jsonObject.getString("price");
                    String genre = jsonObject.getString("genre");
                    String seller_id = jsonObject.getString("seller_id");
                    String seller_name = jsonObject.getString("seller_name");
                    String seller_image = jsonObject.getString("seller_image");
                    String isLiked = jsonObject.getString("isLiked");
                    String card_id = jsonObject.getString("id");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ItemBookData itemBookData = new ItemBookData(card_id,title,method,book_image,
                                    author,price,genre,seller_id,seller_name,seller_image,isLiked);
                            itemBooksData.add(itemBookData);
                            itemBookAdapter = new ItemBookAdapter(itemBooksData,view);
                            recyclerView.setAdapter(itemBookAdapter);
                            itemBookAdapter.notifyDataSetChanged();

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
