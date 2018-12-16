package com.example.kazak.lollipop.Helpers

import android.content.Context
import android.net.Uri
import com.example.kazak.lollipop.Model.User
import com.example.kazak.lollipop.R.id.*
import com.example.kazak.lollipop.View.IDatabaseView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DatabaseHelper() {
    var mDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    var mUsersRef: DatabaseReference = mDatabase.child("users")
    lateinit var cur_user : User
    val uid: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid

    fun saveUser(){
        mUsersRef.child(uid).setValue(cur_user)
    }

    fun loadUserInformation(view : IDatabaseView) {
        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                cur_user = dataSnapshot.getValue(User::class.java)!!
                val userInfo = dataSnapshot.child(uid).getValue(User::class.java)
                if (userInfo != null)
                    cur_user = userInfo
                view.setUser(cur_user)
                view.stopProgressSpinner()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        val currentUserRef = mUsersRef.child(uid)
        currentUserRef.addValueEventListener(userListener)
    }

}