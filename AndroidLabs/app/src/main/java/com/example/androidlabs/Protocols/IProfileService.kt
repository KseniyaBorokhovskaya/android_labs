package com.example.androidlabs.Protocols

import com.example.androidlabs.Models.Profile

interface IProfileService {
    fun askPermissionForUsingCamera()
    fun askPermissionForUsingGallery()

    fun getProfile(): Profile
    fun saveProfileInfo(profile: Profile)

    fun signOut()
}