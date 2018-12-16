package com.example.androidlabs.Models

import com.example.androidlabs.Storage.Entities.Profile

data class Profile(var surname : String, var name : String, var email : String, var phone : String) {

    var imagePath : String = "android.resource://com.example" +
            ".androidLabs/drawable/profile.png"

    constructor() : this("", "", "",
            "")

    constructor(profile: Profile) : this(profile.surname, profile.name, profile.email,
            profile.phone) {
        imagePath = profile.imagePath
    }
}