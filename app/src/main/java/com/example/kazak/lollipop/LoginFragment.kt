package com.example.kazak.lollipop

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.register_fragment.*

class LoginFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<TextView>(R.id.back_to_register_textview)
        button?.setOnClickListener {
            findNavController().navigate(R.id.register_fragment)
        }
        login_button.setOnClickListener{
            performAuthorization()
        }
    }
    private fun performAuthorization (){
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
    }
}