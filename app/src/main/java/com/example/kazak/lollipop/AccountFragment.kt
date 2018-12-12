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
import com.example.kazak.lollipop.Helpers.Constants
import com.example.kazak.lollipop.Helpers.ImageHelper
import com.example.kazak.lollipop.Helpers.PermissionHelper
import com.example.kazak.lollipop.Model.User
import java.io.File
import java.io.IOException
import java.util.*


class AccountFragment: Fragment() {
    lateinit var imageHelper : ImageHelper
    val user = User(name="Vadim", last_name = "Kozachenko",
            phone = "+375296966798", email = "k.a.z.a.k.2013@mail.ru")

    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)
        imageHelper = ImageHelper(activity!!, this)

        return inflater.inflate(R.layout.account_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val permissionHelper = PermissionHelper(activity!!)
        camera_button.setOnClickListener{
            imageHelper.dispatchTakePictureIntent()
        }
        gallery_button.setOnClickListener{
            imageHelper.dispatchSelectPictureIntent()
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
            permissionHelper.requestPhotoPermission()
        }
        email_text_view.text = user.email
        phone_text_view.text = user.phone
        full_name_text_view.text = user.name + " " + user.last_name
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == Constants.REQUEST_IMAGE_CAPTURE) {
                // Do something with imagePath
                //galleryAddPic()
                val f = File(imageHelper.PhotoPath)
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
}