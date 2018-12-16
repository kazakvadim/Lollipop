package com.example.kazak.lollipop.Helpers

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.kazak.lollipop.Helpers.Constants.Companion.CAMERA
import com.example.kazak.lollipop.Helpers.Constants.Companion.GALLERY
import java.io.File
import java.io.IOException
import java.util.*

class ImageHelper(context: Context) {
    private val activity = context as Activity
    private var context: Context
    var PhotoPath : String = ""
    init {
        this.context = context
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        // file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        // directory
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_",".jpg", storageDir)
          .apply {
            PhotoPath = absolutePath
        }
    }

    fun dispatchTakePictureIntent(): Intent  {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = createImageFile()
        val photoURI: Uri = FileProvider.getUriForFile(context,"com.example.android.fileprovider", photoFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        return intent
    }

    fun dispatchSelectPictureIntent() : Intent
    {
        return Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }
}