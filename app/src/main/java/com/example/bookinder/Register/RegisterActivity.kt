package com.example.bookinder.Register

import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookinder.Login.LoginActivity
import com.example.bookinder.R
import com.example.bookinder.api.RetrofitClient
import com.example.bookinder.models.DefaultResponse
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        move_to_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        signup_btn.setOnClickListener {
            val username = signup_user_name.text.toString().trim()
            val email = signup_email.text.toString().trim()
            val password = signup_password.text.toString().trim()
            val confirm_password = signup_confirm_password.text.toString().trim()
            val age = signup_age.text.toString().trim()
            val phone_number = signup_phone.text.toString().trim()

            if(username.isEmpty()) {
                signup_user_name.error = "Username is required"
                signup_user_name.requestFocus()
                return@setOnClickListener
            }

            if(email.isEmpty()) {
                signup_email.error = "Email is required"
                signup_email.requestFocus()
                return@setOnClickListener
            }
            if (!email.isEmailValid()) {
                signup_email.error = "Email is not valid"
                signup_email.requestFocus()
                return@setOnClickListener
            }
            if(password.isEmpty()) {
                signup_password.error = "Password is required"
                signup_password.requestFocus()
                return@setOnClickListener
            }
            if (!isValidPassword(password)) {
                signup_password.error = "The password must include at least one upper and lower " +
                        "case letter, one digit and at least 4 characters"
                signup_password.requestFocus()
                return@setOnClickListener
            }
            if(confirm_password.isEmpty()) {
                signup_confirm_password.error = "You must confirm your password"
                signup_confirm_password.requestFocus()
                return@setOnClickListener
            }
            if (password != confirm_password) {
                signup_password.error = "Password is not match"
                signup_password.requestFocus()
                return@setOnClickListener
            }
            try {
                val age_number = age.toInt()
                if (age_number < 0 || age_number > 120) {
                    signup_age.error="invalid age"
                    signup_age.requestFocus()
                    return@setOnClickListener
                }
            } catch (e: NumberFormatException){
                signup_age.error="age must be a number"
                signup_age.requestFocus()
                return@setOnClickListener
            }
            if(!PhoneNumberUtils.isGlobalPhoneNumber(phone_number)) {
                signup_phone.error = "invalid phone number"
                signup_phone.requestFocus()
                return@setOnClickListener
            }

            RetrofitClient.instance.createUser(username,email,password,age,phone_number).
            enqueue(object: Callback<DefaultResponse>{
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                   Toast.makeText(applicationContext, response.body()?.message,Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    fun isValidPassword(password: String?) : Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }

    fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}