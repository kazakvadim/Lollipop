package com.example.kazak.lollipop

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.hardware.usb.UsbDevice.getDeviceId
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.PersistableBundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.util.jar.Manifest
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log;
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kazak.lollipop.helpers.Constants
import com.example.kazak.lollipop.helpers.PermissionHelper
import com.example.kazak.lollipop.model.User
// import com.example.kazak.lollipop.R.id.IMEIView
// import com.example.kazak.lollipop.R.id.versionView
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissionHelper = PermissionHelper(this)
        val host: NavHostFragment = supportFragmentManager
                .findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController
        setupBottomNavMenu(navController)
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController)
        if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionHelper.requestAllPermission(arrayOf(
                    android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp(findNavController(R.id.my_nav_host_fragment), null)
    }
    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
        bottomNav?.setupWithNavController(navController)
    }
}
