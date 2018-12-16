package com.example.kazak.lollipop.Helpers

import android.util.Log
import android.widget.Toast
import com.example.kazak.lollipop.R.id.email_edittext_register
import com.example.kazak.lollipop.R.id.password_edittext_register
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.register_fragment.*

class AuthorizationHelper {
    private var database: DatabaseReference
    private var auth: FirebaseAuth

    init {
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
    }

    fun performRegister(email: String, password: String){
        if (email.isEmpty() || password.isEmpty()) {
            //Toast.makeText(this, "Please enter email/password", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("RegisterFragment", "Email is " + email)
        Log.d("RegisterFragment", "Password $password")
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if (it.isSuccessful)
                        return@addOnCompleteListener
                    Log.d("Register", "Successfully created user with id")
                }
                .addOnFailureListener{
                    Log.d("Register", "Failed to create user: ${it.message}")
                   // Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

    fun performAuthorization (email: String, password: String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    }

}