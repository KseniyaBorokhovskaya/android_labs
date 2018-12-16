package com.example.androidlabs.Storage

import com.example.androidlabs.Models.Profile
import com.example.androidlabs.Models.User


interface IStorage {
    fun createUser(user: User) : Boolean

    fun authenticate(login: String, password: String) : Boolean
    fun signOut()

    fun saveProfile(profile: Profile)
    fun getProfile() : Profile?

    fun savePhoto(photoPath: String)
}