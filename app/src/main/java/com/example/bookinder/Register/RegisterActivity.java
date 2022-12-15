package com.example.bookinder.Register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookinder.Login.LoginActivity;
import com.example.bookinder.MainActivity;
import com.example.bookinder.ProfileExpansion.ProfileExpansion;
import com.example.bookinder.R;

import androidx.appcompat.app.AppCompatActivity;

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

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button Register = (Button) findViewById(R.id.signup_btn);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(view);
            }});
    }

    public void register(View v) {
        EditText usernameView = findViewById(R.id.signup_email);
        EditText nameView = findViewById(R.id.signup_user_name);
        EditText phoneView = findViewById(R.id.signup_phone);
        EditText passwordView = findViewById(R.id.signup_password);
        EditText ageView = findViewById(R.id.signup_age);
        EditText confirmPasswordView = findViewById(R.id.signup_confirm_password);

        String username = usernameView.getText().toString().trim();
        String name = nameView.getText().toString().trim();
        String phone = phoneView.getText().toString().trim();
        String password = passwordView.getText().toString().trim();
        String confirmPassword = confirmPasswordView.getText().toString().trim();
        String age = ageView.getText().toString().trim();

        boolean flag = true;
        if(name.length() == 0) {
            nameView.setError("Name is required");
            flag = false;
        }
        if(username.length() == 0) {
            usernameView.setError("Email is required");
            flag = false;
        }
        if(password.length() == 0) {
            passwordView.setError("Password is required");
            flag = false;
        }
        if(confirmPassword.length() == 0) {
            confirmPasswordView.setError("You must confirm your password");
            flag = false;
        }
        if(!password.equals(confirmPassword)) {
            confirmPasswordView.setError("The password must be the same");
            flag = false;
        }
        if(age.length() == 0) {
            ageView.setError("Age is required");
            flag = false;
        }
        if(phone.length() == 0) {
            phoneView.setError("Phone number is required");
            flag = false;
        }


        if (!flag) {
            Toast.makeText(getApplicationContext(), "Something is wrong. Please check your inputs.", Toast.LENGTH_LONG).show();
        } else {
            JSONObject registrationForm = new JSONObject();
            try {
                registrationForm.put("subject", "register");
                registrationForm.put("name", name);
                registrationForm.put("email", username);
                registrationForm.put("phone", phone);
                registrationForm.put("password", password);
                registrationForm.put("age", age);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), registrationForm.toString());

            postRequest("http://192.168.1.169:5000", body);
        }
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
                        TextView responseText = findViewById(R.id.responseTextRegister);
                        responseText.setText("Failed to Connect to Server. Please Try Again.");
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) {
                final TextView responseTextRegister = findViewById(R.id.responseTextRegister);
                try {
                    final String responseString = response.body().string().trim();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (responseString.equals("success")) {
                                responseTextRegister.setText("Registration completed successfully.");
                                Intent intent = new Intent(RegisterActivity.this, ProfileExpansion.class);
                                startActivity(intent);
                                finish();
                            } else if (responseString.equals("Another user used this email. Please chose another email.")) {
                                responseTextRegister.setText("Email already exists. Please chose another email.");
                            } else {
                                responseTextRegister.setText("Something went wrong. Please try again later.");
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
