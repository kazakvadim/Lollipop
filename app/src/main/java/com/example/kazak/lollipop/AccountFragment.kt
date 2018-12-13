package com.example.kazak.lollipop

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File
import java.io.IOException
import java.util.*


class AccountFragment: Fragment() {
    lateinit var imageHelper : ImageHelper
    val mDatabase = FirebaseDatabase.getInstance().getReference()
    val mUsersRef = mDatabase.child("users")
    lateinit var cur_user : User


    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)
        imageHelper = ImageHelper(activity!!, this)
        return inflater.inflate(R.layout.account_fragment, container, false)

    }

    val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE ->
            {
                val user = User(
                        phone=edit_phone.text.toString(),
                        email=edit_email.text.toString(),
                        name = cur_user.name.toString(),
                        last_name = cur_user.last_name.toString(),
                        image_uri = cur_user.image_uri.toString()
                )
                mUsersRef.child("Kozachenko").setValue(user)
            }
            DialogInterface.BUTTON_NEGATIVE ->
            {
                edit_email.setText(email_text_view.text)
                edit_phone.setText(phone_text_view.text)
            }
        }
        profile_switcher.showNext()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val userListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                if (user != null){
                    cur_user = user
                }
                cur_user?.let {
                    email_text_view?.text = cur_user.email
                    edit_email?.setText(cur_user.email)
                    phone_text_view?.text = cur_user.phone
                    edit_phone?.setText(cur_user.phone)
                    full_name_text_view?.text = cur_user.name + " " + cur_user.last_name

                    val photoUri = Uri.parse(cur_user.image_uri)
                    avatar_image_view?.setImageURI(photoUri)
                   avatar_image_view1?.setImageURI(photoUri)
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented")
            }
        }
        val currentUserRef = mUsersRef.child("Kozachenko")
        currentUserRef.addValueEventListener(userListener)
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
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Do you want to save data?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .show()
//            profile_switcher.showNext()
        }
        if (ContextCompat.checkSelfPermission(context!!,
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(context!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionHelper.requestPhotoPermission()
        }
//        val user = User(name = "Vadim", last_name = "Kozachenko", email = "k.a.z.a.k.2013@mail.ru", phone = "+375296966798")
//        mDatabase.child("users").child("Kozachenko").setValue(user)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK)
        {
            if (requestCode == Constants.REQUEST_IMAGE_CAPTURE) {
                val f = File(imageHelper.PhotoPath)
                val photoURI = Uri.fromFile(f)
                avatar_image_view.setImageURI(photoURI)
                cur_user.image_uri = photoURI.toString()
            }
            else if (requestCode == Constants.REQUEST_GALLERY_PICK)
            {
                if(data != null)
                {
                    val photoURI : Uri = data.data!!
                    val bmp : Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, photoURI)
                    avatar_image_view.setImageBitmap(bmp)
                    cur_user.image_uri = photoURI.toString()
                }
            }
        }
        mDatabase.child("users").child("Kozachenko").setValue(cur_user)

    }
}