package com.example.kazak.lollipop.View

import com.example.kazak.lollipop.Model.User

interface IDatabaseView {
    fun setUser(user : User)
    fun stopProgressSpinner()
}