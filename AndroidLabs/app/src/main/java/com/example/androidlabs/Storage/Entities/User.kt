package com.example.androidlabs.Storage.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.androidlabs.Models.User

@Entity(tableName = "users")
data class User(
        @ColumnInfo(name = "login") var login: String,
        @ColumnInfo(name = "password") var password: String,
        @ColumnInfo(name = "isCurrent") var isCurrent: Boolean
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0

    constructor(user: User) : this(user.login, user.password, user.isCurrent)
}
