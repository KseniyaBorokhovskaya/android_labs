package com.example.androidlabs.Storage.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidlabs.Storage.Entities.User

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveUser(user: User)

    @Query("SELECT * FROM users WHERE users.isCurrent ")
    fun getCurrentUser() : User?

    @Query("SELECT * FROM users WHERE users.login = :login")
    fun getAuthenticatedUserByLogin(login: String) : User?

}