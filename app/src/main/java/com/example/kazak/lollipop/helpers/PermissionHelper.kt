package com.example.kazak.lollipop.helpers

import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import android.content.Intent
import android.content.DialogInterface
import android.R.string.cancel
import android.R.string.ok
import android.app.Activity
import android.Manifest.permission
import android.Manifest.permission.CALL_PHONE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.widget.Toast
import com.example.kazak.lollipop.R


class PermissionHelper(activity: Activity) {
    private var app_permissions = arrayOf<String>()
    private var activity: Activity? = activity
    init {
        this.activity = activity
    }

    fun requestAllPermission(all_perms: Array<String>){
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                        android.Manifest.permission.READ_PHONE_STATE ) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                        android.Manifest.permission.CAMERA ) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity!!,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE )){
            Toast.makeText(activity!!, "Application needs your permission",
                    Toast.LENGTH_LONG).show()
        }
        ActivityCompat.requestPermissions(activity!!,
                all_perms, Constants.ALL_PERMISSION_CODE)
    }
}