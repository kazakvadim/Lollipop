package com.example.kazak.lollipop

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import kotlinx.android.synthetic.main.account_fragment.*
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.kazak.lollipop.helpers.Constants
import com.example.kazak.lollipop.helpers.PermissionHelper
import com.example.kazak.lollipop.model.User
import java.io.File
import java.io.IOException
import java.util.*


class AccountFragment: Fragment() {
    var PhotoPath : String = ""
    val user = User(name="Vadim", last_name = "Kozachenko",
            phone = "+375296966798", email = "k.a.z.a.k.2013@mail.ru")

    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.account_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        camera_button.setOnClickListener{
            dispatchTakePictureIntent()
        }

        gallery_button.setOnClickListener{
            dispatchSelectPictureIntent()
        }
        change_profile.setOnClickListener{
            profile_switcher.showNext()
        }
        change_profile1.setOnClickListener{
            profile_switcher.showNext()
        }
        if (ContextCompat.checkSelfPermission(context!!,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPhotoPermission()
        }
        email_text_view.text = user.email
        phone_text_view.text = user.phone
        full_name_text_view.text = user.name + " " + user.last_name
    }

    fun requestPhotoPermission(){
        val permissionHelper = PermissionHelper(activity!!)
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) ||
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(context, getString(R.string.need_permission),
                    Toast.LENGTH_LONG).show()
        }
        permissionHelper.requestAllPermission(arrayOf(android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == Constants.REQUEST_IMAGE_CAPTURE) {
                // Do something with imagePath
                //galleryAddPic()
                val f = File(PhotoPath)
                val ur = Uri.fromFile(f)
                avatar_image_view.setImageURI(ur)
                //val photo = data?.getExtras()?.get("data") as Bitmap
                //imageView.setImageBitmap(photo)
            }
            else if (requestCode == Constants.REQUEST_GALLERY_PICK)
            {
                if(data != null)
                {
                    val photoURI : Uri = data.data!!
                    val bmp : Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, photoURI)
                    avatar_image_view.setImageBitmap(bmp)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        // directory
        val storageDir: File? = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val file: File? = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
//        if (file != null){
//            PhotoPath = file.absolutePath
//        }
//        return file!!
        return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
        ).apply {
            PhotoPath = absolutePath
        }
    }

    private fun dispatchSelectPictureIntent()
    {
        if (ContextCompat.checkSelfPermission(context!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            val frag: Fragment = this
            val intent: Intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            frag.startActivityForResult(intent, Constants.REQUEST_GALLERY_PICK)
        }
        else {
            requestPhotoPermission()
        }
    }

    private fun dispatchTakePictureIntent() {
        if (ContextCompat.checkSelfPermission(context!!,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            val fragment: Fragment = this
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(activity!!.packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }
                    // if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                                context!!,
                                "com.example.android.fileprovider",
                                it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        fragment.startActivityForResult(takePictureIntent, Constants.REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }
        else {
            requestPhotoPermission()
        }

    }
}