package com.example.telegram.models

data class User(
    val id: String = "",
    var username: String = "",
    var fullname: String = "",
    var bio: String = "",
    var state: String = "",
    var phone: String = "",
    var photoUrl: String = "",
)