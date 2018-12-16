package com.example.androidlabs.Storage.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidlabs.Models.Profile

@Entity(tableName = "profiles")
data class Profile(
        @ColumnInfo(name = "surname") var surname: String,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "email") var email: String,
        @ColumnInfo(name = "phone") var phone: String,
        @ColumnInfo(name = "imagePath") var imagePath: String,
        @ColumnInfo(name = "userId") var userId: Int
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0

    constructor(profile: Profile, userId: Int) : this(profile.surname, profile.name, profile.email,
            profile.phone, profile.imagePath, userId)

    constructor(userId: Int) : this("", "", "",
            "", "android.resource://com.example" +
            ".androidLabs/drawable/profile.png", userId)

}