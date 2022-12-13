package com.example.bookinder.Login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinder.MainActivity
import com.example.bookinder.R
import com.example.bookinder.Register.RegisterActivityko
import com.example.bookinder.api.RetrofitClient
import com.example.bookinder.models.LoginResponse
import com.example.bookinder.storage.SharedPrefManager
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivityKo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signinBtn.setOnClickListener{
            startActivity(Intent(this@LoginActivityKo, RegisterActivityko::class.java))
        }

        login_btn.setOnClickListener{
            val email = login_email.text.toString().trim()
            val password = login_password.text.toString().trim()

            if(email.isEmpty()) {
                login_email.error = "Email required"
                login_email.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()) {
                login_password.error = "password required"
                login_password.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.userLogin(email,password).enqueue(object: Callback<LoginResponse>{
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.body()?.error!!){
                        SharedPrefManager.getInstance(applicationContext).saveUser(response.body()?.user!!)

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)

                    }else {
                        Toast.makeText(applicationContext, response.body()?.message, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

            })
        }

    }
    override fun onStart() {
        super.onStart()

        if(SharedPrefManager.getInstance(this).isLoggedIn){
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}