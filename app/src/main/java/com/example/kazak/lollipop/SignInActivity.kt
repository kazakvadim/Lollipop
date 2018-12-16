package com.example.kazak.lollipop

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.kazak.lollipop.Helpers.AuthorizationHelper
import com.example.kazak.lollipop.Helpers.PermissionHelper
import com.example.kazak.lollipop.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.register_fragment.*

class SignInActivity : AppCompatActivity() {

    //lateinit var authorizationHelper : AuthorizationHelper
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var progressSpinner: ProgressSpinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        progressSpinner = ProgressSpinner(this)
        //authorizationHelper = AuthorizationHelper()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return item.onNavDestinationSelected(findNavController(R.id.signin_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }
    public override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            startActivity(Intent(this@SignInActivity, MainActivity::class.java))
            finish()
        }
    }


    fun onAuthSuccess(user: FirebaseUser) {
        val userId = user.uid
        val email = user.email
        val name = name_edittext_register.text.toString()
        val last_name = lastname_edittext_register.text.toString()
        val new_user = User(name=name, last_name =last_name, email = email)
        database.child("users").child(userId).setValue(new_user)
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
        finish()
    }

    fun performRegister(){
        progressSpinner.showProgressDialog()
        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email/password", Toast.LENGTH_SHORT).show()
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful)
                    {
                        progressSpinner.hideProgressDialog()
                        onAuthSuccess(task.result?.user!!)
                    }
                        //return@addOnCompleteListener
                    Log.d("Register", "Successfully created user with id")
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                }
                .addOnFailureListener{
                    Log.d("Register", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

    fun performAuthorization (){
        progressSpinner.showProgressDialog()
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful){
                    progressSpinner.hideProgressDialog()
                    startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                }
                else{
                    Toast.makeText(baseContext, "Sign In Failed",
                            Toast.LENGTH_SHORT).show()
                }
        }

    }
}
