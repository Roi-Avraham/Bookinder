//package com.example.bookinder.Login
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.TextView
//import androidx.appcompat.app.AppCompatActivity
//import com.example.bookinder.MainActivity
//import com.example.bookinder.R
//import com.example.bookinder.Register.RegisterActivityko
//import kotlinx.android.synthetic.main.activity_login.*
//import okhttp3.*
//import org.json.JSONException
//import org.json.JSONObject
//import java.io.IOException
//import java.lang.ref.Cleaner.create
//
//
//class Login : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//        signinBtn.setOnClickListener{
//            startActivity(Intent(this@Login, RegisterActivityko::class.java))
//        }
//
//        login_btn.setOnClickListener{
//            val email = login_email.text.toString().trim()
//            val password = login_password.text.toString().trim()
//
//            if(email.isEmpty()) {
//                login_email.error = "Email required"
//                login_email.requestFocus()
//                return@setOnClickListener
//            }
//
//            if(password.isEmpty()) {
//                login_password.error = "password required"
//                login_password.requestFocus()
//                return@setOnClickListener
//            }
//            val loginForm = JSONObject()
//            try {
//                loginForm.put("subject", "login")
//                loginForm.put("email", email)
//                loginForm.put("password", password)
//            } catch (e: JSONException) {
//                e.printStackTrace()
//            }
//
//            val body: RequestBody = create(MediaType.parse("application/json; charset=utf-8"), loginForm.toString())
//
//            postRequest(MainActivity.postUrl, body)
//        }
//    }
//    fun postRequest(postUrl: String?, postBody: RequestBody?) {
//        val client = OkHttpClient()
//        val request: Request = OkHttpClient.Builder()
//                .url(postUrl)
//                .post(postBody)
//                .header("Accept", "application/json")
//                .header("Content-Type", "application/json")
//                .build()
//        client.newCall(request).enqueue(object : Callback {
//            fun onFailure(call: Call, e: IOException) {
//                // Cancel the post on failure.
//                call.cancel()
//
//                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
//                runOnUiThread {
//                    val responseTextLogin = findViewById<TextView>(com.example.bookinder.R.id.responseTextLogin)
//                    responseTextLogin.text = "Failed to Connect to Server. Please Try Again."
//                }
//            }
//
//            @Throws(IOException::class)
//            fun onResponse(call: Call?, response: Response) {
//                // In order to access the TextView inside the UI thread, the code is executed inside runOnUiThread()
//                runOnUiThread {
//                    val responseTextLogin = findViewById<TextView>(com.example.bookinder.R.id.responseTextLogin)
//                    try {
//                        val loginResponseString: String = response.body.string().trim()
//                        Log.d("LOGIN", "Response from the server : $loginResponseString")
//                        if (loginResponseString == "success") {
//                            Log.d("LOGIN", "Successful Login")
//                            finish() //finishing activity and return to the calling activity.
//                        } else if (loginResponseString == "failure") {
//                            responseTextLogin.text = "Login Failed. Invalid username or password."
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                        responseTextLogin.text = "Something went wrong. Please try again later."
//                    }
//                }
//            }
//        })
//    }
//
//}