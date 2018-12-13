package com.example.kazak.lollipop.Helpers

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.IOException
import java.util.*

class ImageHelper(activity: Activity, fragment: Fragment) {
    var PhotoPath : String = ""
    var permissionHelper = PermissionHelper(activity)
    private var activity: Activity = activity
    private var fragment: Fragment = fragment
    init {
        this.activity = activity
        this.fragment = fragment
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        // file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        // directory
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_",".jpg", storageDir)
          .apply {
            PhotoPath = absolutePath
        }
    }

    fun dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile = createImageFile()
            val photoURI: Uri = FileProvider.getUriForFile(activity,"com.example.android.fileprovider", photoFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            fragment.startActivityForResult(intent, Constants.REQUEST_IMAGE_CAPTURE)
        }
        else {
            permissionHelper.requestPhotoPermission()
        }
    }

    fun dispatchSelectPictureIntent()
    {
        if (checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            val intent: Intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            fragment.startActivityForResult(intent, Constants.REQUEST_GALLERY_PICK)
        }
        else {
            permissionHelper.requestPhotoPermission()
        }
    }
}



//    fun dispatchTakePictureIntent() {
//        if (ContextCompat.checkSelfPermission(activity,
//                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(activity,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
//        {
//            val frag: Fragment = fragment
//            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//                // Ensure that there's a camera activity to handle the intent
//                takePictureIntent.resolveActivity(activity.packageManager)?.also {
//                    // Create the File where the photo should go
//                    val photoFile: File? = try {
//                        createImageFile()
//                    } catch (ex: IOException) {
//                        // Error occurred while creating the File
//                        null
//                    }
//                    // Continue only if the File was successfully created
//                    photoFile?.also {
//                        val photoURI: Uri = FileProvider.getUriForFile(
//                                activity,
//                                "com.example.android.fileprovider",
//                                it
//                        )
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
//                        frag.startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE)
//                    }
//                }
//            }
//        }
//        else {
//            permissionHelper.requestPhotoPermission()
//        }
//
//    }
