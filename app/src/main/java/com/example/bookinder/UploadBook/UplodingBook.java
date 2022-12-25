package com.example.bookinder.UploadBook;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.bookinder.MainActivity;
import com.example.bookinder.MyProfile.MyProfileActivity;
import com.example.bookinder.MyProfile.ui.Profile.ProfileFragment;
import com.example.bookinder.Profile.Profile_activity;
import com.example.bookinder.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

public class UplodingBook extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    ImageView bookPicture;
    ActivityResultLauncher<String> mTakePhoto;
    RadioButton genderradioButton;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploding_book);
        radioGroup= findViewById(R.id.radioGroup);

        Intent intent = getIntent();
        String current_user = intent.getStringExtra("current_user");

        /******************************************/
        bottomNavigationView = findViewById(R.id.button_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.nav_cart);
        bottomNavigationView.getMenu().findItem(R.id.nav_cart).setChecked(true);
        /******************************************/

        String book_name = findViewById(R.id.name_of_the_book).toString();
        String writer_name = findViewById(R.id.name_of_writer).toString();
        EditText priceEdit = findViewById(R.id.price);
        String price = priceEdit.getText().toString().trim();
        Button uploadBookImage = findViewById(R.id.upload);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderradioButton = findViewById(selectedId);
        RadioButton sale = findViewById(R.id.sale);
        RadioButton exchange = findViewById(R.id.exchange);
        bookPicture = findViewById(R.id.bookImage);
        Button done = findViewById(R.id.doneBtn);

        //get the spinner from the xml.
        Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"Novel", "History", "Thriller","Science","Fantasy","Children"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        bookPicture.setImageURI(result);
                        if(result!=null) {
                            System.out.println(String.valueOf(dropdown.getSelectedItem()));
                            //sendBookImage(result,current_user);
                        }
                    }
                }
        );
        uploadBookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTakePhoto.launch("image/*");
            }
        });


        done.setOnClickListener(view -> {
            String genre = String.valueOf(dropdown.getSelectedItem());
            if(sale.isChecked()) {
                if (price.length() == 0) {
                    priceEdit.setError("You must enter price");
                }
                sendToServer(current_user,book_name,writer_name,genre,"sale",price);
            } else {
                sendToServer(current_user,book_name,writer_name,genre,"exchange","0");
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);
        EditText priceEdit = findViewById(R.id.price);
        if (radioButton.getId()== R.id.sale) {
            priceEdit.setEnabled(true);
            priceEdit.setFocusable(true);
        } else {
            priceEdit.setEnabled(false);
        }
    }

    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_home:
                intent = new  Intent(UplodingBook.this, MainActivity.class);
                UplodingBook.this.startActivity(intent);
                return true;
            case R.id.nav_person:
                intent = new  Intent(UplodingBook.this, MyProfileActivity.class);
                UplodingBook.this.startActivity(intent);
                return true;
        }
        return false;
    }
    public void sendBookImage(Uri result, String current_user) {
        JSONObject bookImageForm = new JSONObject();
        try {
            bookImageForm.put("current_user", current_user);
            bookImageForm.put("book_image", result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), bookImageForm.toString());

        postRequest("http://192.168.1.169:5000/getBookImage", body);
    }
    public  void sendToServer(String current_user, String book_name, String writer_name,
                              String genre, String method, String price) {
        JSONObject uploadBookForm = new JSONObject();
        try {
            uploadBookForm.put("user_id", current_user);
            uploadBookForm.put("name_of_book", book_name);
            uploadBookForm.put("name_of_writer",writer_name);
            uploadBookForm.put("method",method);
            uploadBookForm.put("genre", genre);
            uploadBookForm.put("price",price);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), uploadBookForm.toString());

        postRequest("http://192.168.1.169:5000/getBookImage", body);
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