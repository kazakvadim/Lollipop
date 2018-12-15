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
import android.net.Uri
import com.example.kazak.lollipop.Helpers.Constants
import com.example.kazak.lollipop.Helpers.Constants.Companion.CAMERA
import com.example.kazak.lollipop.Helpers.Constants.Companion.GALLERY
import com.example.kazak.lollipop.Helpers.ImageHelper
import com.example.kazak.lollipop.Helpers.PermissionHelper
import com.example.kazak.lollipop.Model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.File
import androidx.core.app.NotificationCompat.getExtras


class AccountFragment: Fragment() {
    lateinit var imageHelper : ImageHelper
    val mDatabase = FirebaseDatabase.getInstance().getReference()
    val mUsersRef = mDatabase.child("users")
    lateinit var cur_user : User
    var tempUri : String? = null


    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)
        imageHelper = ImageHelper(context!!, this)
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
                if (tempUri != null){
                    avatar_image_view?.setImageURI(Uri.parse(tempUri))
                    cur_user.image_uri = tempUri
                }
                mUsersRef.child("Kozachenko").setValue(user)
            }
            DialogInterface.BUTTON_NEGATIVE ->
            {
                edit_email.setText(email_text_view.text)
                edit_phone.setText(phone_text_view.text)
                avatar_image_view1.setImageURI(Uri.parse(cur_user.image_uri))
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
                cur_user.let {
                    email_text_view?.text = it.email
                    edit_email?.setText(it.email)
                    phone_text_view?.text = it.phone
                    edit_phone?.setText(it.phone)
                    name_text_view?.text = it.name
                    edit_name?.setText(it.name)
                    last_name_text_view?.text = it.last_name
                    edit_last_name?.setText(it.last_name)
                   // full_name_text_view?.text = it.name + " " + it.last_name

                    val photoUri = Uri.parse(it.image_uri)
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

        val permissionHelper = PermissionHelper(context as Activity)
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

        if (context?.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                context?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionHelper.requestPhotoPermission()
        }
//        val user = User(name = "Vadim", last_name = "Kozachenko", email = "k.a.z.a.k.2013@mail.ru", phone = "+375296966798")
//        mDatabase.child("users").child("Kozachenko").setValue(user)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {
                val f = File(imageHelper.PhotoPath)
                val photoURI = Uri.fromFile(f)
                avatar_image_view.setImageURI(photoURI)
                cur_user.image_uri = photoURI.toString()
                avatar_image_view1.setImageURI(photoURI)
                tempUri = photoURI.toString()
            }
            else if (requestCode == GALLERY) {
                val photoURI: Uri? = data?.data
                val bmp: Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, photoURI)
                avatar_image_view.setImageBitmap(bmp)
                avatar_image_view1.setImageBitmap(bmp)
                tempUri = photoURI.toString()
                // cur_user.image_uri = photoURI.toString()
            }
        }
    }
}