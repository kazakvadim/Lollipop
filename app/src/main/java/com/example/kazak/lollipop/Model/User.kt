package com.example.kazak.lollipop.Model

data class User(
        var last_name: String? = "",
        var name: String? = "",
        var email: String? = "",
        var phone: String? = "",
        var image_uri: String? = ""
)

//
//class User(name: String, last_name: String,
//           email:String, phone:String) {
//    var name: String? = null
//    var last_name: String? = null
//    var email: String? = null
//    var phone: String? = null
//    var image_uri: String? = null
//    init {
//        this.name = name
//        this.last_name = last_name
//        this.email = email
//        this.phone = phone
//    }
//}