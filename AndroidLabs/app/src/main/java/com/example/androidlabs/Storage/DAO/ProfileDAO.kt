package com.example.androidlabs.Storage.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidlabs.Storage.Entities.Profile

@Dao
interface ProfileDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProfile(profile: Profile)

    @Query("SELECT * FROM users, profiles WHERE users.id = profiles.userId AND users.isCurrent")
    fun getProfile() : Profile?

}