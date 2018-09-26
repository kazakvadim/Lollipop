package com.example.kazak.lollipop

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.hardware.usb.UsbDevice.getDeviceId
import android.content.Context.TELEPHONY_SERVICE
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {
    val IMEI_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.versionView).apply {
            text = BuildConfig.VERSION_NAME
        }
        if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show()
            getIMEI()
        } else {
            requestImeiPermission();
        }
        if (BuildConfig.FLAVOR == "stable"){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        } else if (BuildConfig.FLAVOR == "developer")
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
        }
    }

    fun getIMEI(){
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imei = telephonyManager.deviceId
        findViewById<TextView>(R.id.IMEIView).apply {
            text = imei
        }
    }

    fun request_button(view: View){
        if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "You have already granted this permission!",
                    Toast.LENGTH_SHORT).show()
        } else {
            requestImeiPermission();
        }
    }

    fun requestImeiPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.READ_PHONE_STATE)){
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_PHONE_STATE), IMEI_PERMISSION_CODE)
        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.READ_PHONE_STATE), IMEI_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == IMEI_PERMISSION_CODE){
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                getIMEI()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }



}
