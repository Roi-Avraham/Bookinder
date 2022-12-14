//package com.example.bookinder.Login
//
//import android.app.ProgressDialog
//import android.content.Intent
//import android.os.Bundle
//import android.text.TextUtils
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.bookinder.MainActivity
//import com.example.bookinder.R
//import com.example.bookinder.Register.RegisterActivityFireBase
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//import kotlinx.android.synthetic.main.activity_login.*
//
//class LoginActivityFireBase : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//        Firebase.auth.signOut()
//
//        signinBtn.setOnClickListener {
//            startActivity(Intent(this,RegisterActivityFireBase::class.java))
//        }
//        login_btn.setOnClickListener {
//            loginUser()
//        }
//    }
//
//    private fun loginUser() {
//        val email = login_email.text.toString()
//        val password = login_password.text.toString()
//
//        when {
//            TextUtils.isEmpty(email) -> Toast.makeText(
//                    this,
//                    "Username is required",
//                    Toast.LENGTH_SHORT
//            ).show()
//            TextUtils.isEmpty(password) -> Toast.makeText(
//                    this,
//                    "Password is required",
//                    Toast.LENGTH_SHORT
//            ).show()
//
//            else -> {
//                val progressDialog = ProgressDialog(this@LoginActivityFireBase)
//                progressDialog.setTitle("Login")
//                progressDialog.setMessage("Logging in...")
//                progressDialog.setCanceledOnTouchOutside(false)
//                progressDialog.show()
//
//                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
//                mAuth.signInWithEmailAndPassword(email, password)
//                        .addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                progressDialog.dismiss()
//                                val intent = Intent(this@LoginActivityFireBase, MainActivity::class.java)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                                startActivity(intent)
//                                finish()
//                            } else {
//                                val message = task.exception!!.toString()
//                                Toast.makeText(this, "Password or Email Invalid", Toast.LENGTH_LONG).show()
//                                mAuth.signOut()
//                                progressDialog.dismiss()
//                            }
//                        }
//            }
//        }
//
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        if(FirebaseAuth.getInstance().currentUser!=null)
//        {
//            //forwarding to home page
//            val intent=Intent(this@LoginActivityFireBase,MainActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
//            finish()
//        }
//    }
//}