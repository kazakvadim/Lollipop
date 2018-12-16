package com.example.kazak.lollipop

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kazak.lollipop.Helpers.PermissionHelper
import com.example.kazak.lollipop.R.id.toggle
// import com.example.kazak.lollipop.R.id.IMEIView
// import com.example.kazak.lollipop.R.id.versionView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.account_fragment.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissionHelper = PermissionHelper(this)
        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return
        navController = host.navController
        setupBottomNavMenu(navController!!)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController!!)
        if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionHelper.requestAllPermission(arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.bar_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val navHostfragment = this.supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
        val fr = navHostfragment?.childFragmentManager?.fragments?.get(0)
        when (id) {
            R.id.infoFragment -> {
                if (fr is AccountFragment && fr.checkNeedToUpdateUser())
                {
                    validNeedToSaveUser(R.id.infoFragment)
                }
                else
                    navController?.navigate(R.id.infoFragment)
                return true
            }
            R.id.logout_button -> {
                if (fr is AccountFragment && fr.checkNeedToUpdateUser())
                    validNeedToSaveUser(-1)
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@MainActivity, SignInActivity::class.java))
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(findNavController(R.id.my_nav_host_fragment), null)
    }


    private fun validNeedToSaveUser(fragment_id: Int) {
        val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE ->
                {
                    (getCurrentFragment() as AccountFragment).saveUser()
                }
                DialogInterface.BUTTON_NEGATIVE ->
                {

                }
            }
            if (fragment_id != -1)
                navController?.navigate(fragment_id)
        }
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("Do you want to save data?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show()
    }
    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
        bottom_nav_view.setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    val navHostfragment = this.supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
                    val fr = navHostfragment?.childFragmentManager?.fragments?.get(0)
                    when (item.itemId) {
                        R.id.nav_home -> {
                            if (fr is AccountFragment && fr.checkNeedToUpdateUser()){
                                validNeedToSaveUser(item.itemId)
//                                navController.navigate(item.itemId)
                            }
                            else{
                                navController.navigate(R.id.nav_home)
                            }
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.nav_account -> {
                            if (fr is AccountFragment && fr.checkNeedToUpdateUser()){
                                validNeedToSaveUser(item.itemId)
//                                navController.navigate(item.itemId)
                            }
                            else {
                                navController.navigate(R.id.nav_account)
                            }
                            return@OnNavigationItemSelectedListener true
                        }
                    }
                    false
                })
    }


    private fun getCurrentFragment(): Fragment {
        val navHostfragment = this.supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment)
        return navHostfragment?.childFragmentManager?.fragments?.get(0)!!
    }

}
