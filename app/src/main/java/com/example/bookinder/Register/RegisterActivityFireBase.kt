//package com.example.bookinder.Register
//
//import android.app.ProgressDialog
//import android.content.Intent
//import android.os.Bundle
//import android.text.TextUtils
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.bookinder.ProfileExpansion.ProfileExpansion
//import com.example.bookinder.R
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.FirebaseDatabase
//import kotlinx.android.synthetic.main.activity_register.*
//
//
//class RegisterActivityFireBase : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
//
//        signup_btn.setOnClickListener {
//            createAccount()
//        }
//    }
//
//    private fun createAccount() {
//        val fullName=signup_fullname.text.toString()
//        val email=signup_email.text.toString()
//        val password=signup_password.text.toString()
//        val confirm_password = signup_confirm_password.text.toString()
//
//
//        when{
//            TextUtils.isEmpty(fullName)-> Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show()
//            TextUtils.isEmpty(email)-> Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
//            TextUtils.isEmpty(password)-> Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
//            TextUtils.isEmpty(confirm_password)-> Toast.makeText(this, "You must confirm your password", Toast.LENGTH_SHORT).show()
//            !password.equals(confirm_password) -> Toast.makeText(this, "Confirm password is not correct", Toast.LENGTH_SHORT).show()
//
//            else->
//            {
//                val progressDialog= ProgressDialog(this@RegisterActivityFireBase)
//                progressDialog.setTitle("SignUp")
//                progressDialog.setMessage("Please wait...")
//                progressDialog.setCanceledOnTouchOutside(false)
//                progressDialog.show()
//
//                val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
//                mAuth.createUserWithEmailAndPassword(email,password)
//                        .addOnCompleteListener { task ->
//                            if(task.isSuccessful)
//                            {
//                                saveUserInfo(fullName,email,progressDialog)
//                                progressDialog.dismiss()
//                                val intent=Intent(this@RegisterActivityFireBase,ProfileExpansion::class.java)
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                                startActivity(intent)
//                            }
//                            else
//                            {
//                                val message=task.exception!!.toString()
//                                Toast.makeText(this,"Error : $message", Toast.LENGTH_LONG).show()
//                                mAuth.signOut()
//                                progressDialog.dismiss()
//                            }
//                        }
//            }
//        }
//    }
//
//    private fun saveUserInfo(fullName: String, email: String,progressDialog:ProgressDialog) {
//        val currentUserId= FirebaseAuth.getInstance().currentUser!!.uid
//        val userRef : DatabaseReference= FirebaseDatabase.getInstance().reference.child("Users")
//
//        val userMap=HashMap<String,Any>()
//        userMap["uid"]=currentUserId
//        userMap["fullname"]=fullName
//        userMap["email"]=email
//        userMap["bio"]="Hey! I am using Bookinder"
//        //userMap["image"]="gs://instagram-clone-app-205f9.appspot.com/Default images/profile.png"
//
//
//        userRef.child(currentUserId).setValue(userMap)
//                .addOnCompleteListener {task ->
//                    if(task.isSuccessful)
//                    {
//                        Toast.makeText(this,"Account has been created",Toast.LENGTH_SHORT).show()
//
//
//                        FirebaseDatabase.getInstance().reference
//                                .child("Follow").child(currentUserId)
//                                .child("Following").child(currentUserId)
//                                .setValue(true)
//
//
//                        val intent=Intent(this@RegisterActivityFireBase,ProfileExpansion::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                        startActivity(intent)
//                        finish()
//                    }
//                    else
//                    {
//                        val message=task.exception!!.toString()
//                        Toast.makeText(this,"Error : $message", Toast.LENGTH_LONG).show()
//                        FirebaseAuth.getInstance().signOut()
//                        progressDialog.dismiss()
//                    }
//                }
//    }
//}
//
