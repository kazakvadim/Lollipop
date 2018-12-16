package com.example.kazak.lollipop

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
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
import java.io.File
import androidx.core.app.NotificationCompat.getExtras
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import com.example.kazak.lollipop.Helpers.DatabaseHelper
import com.example.kazak.lollipop.View.IDatabaseView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.register_fragment.*




class AccountFragment: Fragment(), IDatabaseView {
    lateinit var imageHelper : ImageHelper
    lateinit var databaseHelper : DatabaseHelper
    lateinit var progressSpinner: ProgressSpinner
    var tempUri : String? = null


    override fun onCreateView(inflater: LayoutInflater,
                              container : ViewGroup?,
                              savedInstanceState: Bundle?) : View? {
        setHasOptionsMenu(true)

        databaseHelper = DatabaseHelper()
        imageHelper = ImageHelper(context!!)
        progressSpinner = ProgressSpinner(context!!)
        return inflater.inflate(R.layout.account_fragment, container, false)
    }


    val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE ->
            {
                SaveChangedUserFields()
            }
            DialogInterface.BUTTON_NEGATIVE ->
            {
                RestoreUserFields()
            }
        }
        profile_switcher.showNext()
    }

    fun saveUser(){
        SaveChangedUserFields()
        //databaseHelper.saveUser()
    }

    private fun SaveChangedUserFields(){
        databaseHelper.cur_user.name = edit_name.text.toString()
        databaseHelper.cur_user.last_name = edit_last_name.text.toString()
        databaseHelper.cur_user.phone = edit_phone.text.toString()
        if (tempUri != null){
            avatar_image_view?.setImageURI(Uri.parse(tempUri))
            databaseHelper.cur_user.image_uri = tempUri
        }
        databaseHelper.saveUser()
    }

    private fun RestoreUserFields(){
        edit_name.setText(name_text_view.text)
        edit_last_name.setText(last_name_text_view.text)
        edit_phone.setText(phone_text_view.text)
        avatar_image_view1.setImageURI(Uri.parse(databaseHelper.cur_user.image_uri))
    }

    override fun setUser(user: User){
        phone_text_view?.text = user.phone
        edit_phone?.setText(user.phone)
        name_text_view?.text = user.name
        edit_name?.setText(user.name)
        last_name_text_view?.text = user.last_name
        edit_last_name?.setText(user.last_name)
        val photoUri = Uri.parse(user.image_uri)
        val file = File(photoUri.getPath())
        if (file.exists()){
        avatar_image_view?.setImageURI(photoUri)
        avatar_image_view1?.setImageURI(photoUri)
        }
        else {
           avatar_image_view?.setImageResource(R.drawable.avatar)
           avatar_image_view1?.setImageResource(R.drawable.avatar)
        }
    }

    fun checkNeedToUpdateUser(): Boolean {
        val cur_user = databaseHelper.cur_user
        val name = edit_name.text.toString()
        val lastname = edit_last_name.text.toString()
        val phone_number = edit_phone.text.toString()
        return !cur_user.name.equals(name) ||
                !cur_user.phone.equals(phone_number) ||
                !cur_user.last_name.equals(lastname)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        progressSpinner.showProgressDialog()
        databaseHelper.loadUserInformation(this)
        val permissionHelper = PermissionHelper(context as Activity)
        camera_button.setOnClickListener{
            if (checkSelfPermission(context!!,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(context!!,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(imageHelper.dispatchTakePictureIntent(), CAMERA)
            }
            else{
                permissionHelper.requestPhotoPermission()
            }
        }
        gallery_button.setOnClickListener{
            if (checkSelfPermission(context!!,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                startActivityForResult(imageHelper.dispatchSelectPictureIntent(), GALLERY)
            }
            else {
                permissionHelper.requestPhotoPermission()
            }
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
        }
        save_button.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Do you want to save data?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .show()
        }

        if (context?.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                context?.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionHelper.requestPhotoPermission()
        }
        progressSpinner.hideProgressDialog()
    }

    override fun stopProgressSpinner() {
        progressSpinner.hideProgressDialog()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {
                val f = File(imageHelper.PhotoPath)
                val photoURI = Uri.fromFile(f)
                avatar_image_view.setImageURI(photoURI)
                databaseHelper.cur_user.image_uri = photoURI.toString()
                avatar_image_view1.setImageURI(photoURI)
                tempUri = photoURI.toString()
            }

            else if (requestCode == GALLERY) {
                if(data != null)
                {
                    val photoURI : Uri = data.data!!
                    val bmp: Bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, photoURI)
                    avatar_image_view.setImageBitmap(bmp)
                    avatar_image_view1.setImageBitmap(bmp)
                    tempUri = photoURI.toString()
                }

            }
        }
    }
}