package com.example.kazak.lollipop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.register_fragment.*

class RegisterFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        register_button.setOnClickListener{
           performRegister()
        }
        val button = view.findViewById<TextView>(R.id.already_have_account_text_view)
        button?.setOnClickListener {
            findNavController().navigate(R.id.login_fragment)
        }
    }
    private fun performRegister(){
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please enter email/password", Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("RegisterFragment", "Email is " + email)
        Log.d("RegisterFragment", "Password $password")
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if (it.isSuccessful) return@addOnCompleteListener
                    // else if successful
                    Log.d("Register", "Successfully created user with id")
                }
                .addOnFailureListener{
                    Log.d("Register", "Failed to create user: ${it.message}")
                    Toast.makeText(context, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()

                }

    }
}