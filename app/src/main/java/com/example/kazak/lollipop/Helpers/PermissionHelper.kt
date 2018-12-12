package com.example.kazak.lollipop.Helpers

import android.Manifest
import androidx.core.app.ActivityCompat
import android.app.Activity
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.example.kazak.lollipop.R


class PermissionHelper(activity: Activity) {
    private var activity: Activity = activity
    init {
        this.activity = activity
    }

    fun requestAllPermission(all_perms: Array<String>){
        if (shouldShowRequestPermissionRationale(activity,
                        android.Manifest.permission.READ_PHONE_STATE ) ||
                shouldShowRequestPermissionRationale(activity,
                        android.Manifest.permission.CAMERA ) ||
                shouldShowRequestPermissionRationale(activity,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE )){
            Toast.makeText(activity, "Application needs your permission",
                    Toast.LENGTH_LONG).show()
        }
        requestPermissions(activity,
                all_perms, Constants.ALL_PERMISSION_CODE)
    }

    fun requestPhotoPermission(){
        if (shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA) ||
                shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(activity, "Application needs your permission",
                    Toast.LENGTH_LONG).show()
        }
        requestAllPermission(arrayOf(android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }
}