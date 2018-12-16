package com.example.androidlabs.Models

import com.example.androidlabs.Storage.Entities.User

data class User(var login : String, var password : String, var isCurrent : Boolean) {

    constructor(user: User) : this(user.login, user.password, user.isCurrent)

}