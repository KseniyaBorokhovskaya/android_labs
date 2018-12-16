package com.example.androidlabs.Storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidlabs.Storage.DAO.ProfileDAO
import com.example.androidlabs.Storage.DAO.UserDAO
import com.example.androidlabs.Storage.Entities.Profile
import com.example.androidlabs.Storage.Entities.User

@Database(entities = [User::class, Profile::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDAO(): UserDAO
    abstract fun profileDAO(): ProfileDAO

    companion object {
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "androidAppDatabase"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }
            return instance as AppDatabase
        }

        fun destroyDatabase() {
            instance = null
        }
    }
}