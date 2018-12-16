package com.example.androidlabs.Storage

import com.example.androidlabs.Models.Profile
import com.example.androidlabs.Models.User
import org.mindrot.jbcrypt.BCrypt

class SQLStorage(private val appDataBase: AppDatabase) : IStorage {
    override fun signOut() {
        val user = appDataBase.userDAO().getCurrentUser() ?: return

        user.isCurrent = false
        appDataBase.userDAO().saveUser(user)
    }

    override fun createUser(user: User) : Boolean {
        if (appDataBase.userDAO().getAuthenticatedUserByLogin(user.login) != null) return false

        user.password = BCrypt.hashpw(user.password, BCrypt.gensalt())
        appDataBase.userDAO().saveUser(com.example.androidlabs.Storage.Entities.User(user))

        val newUser = appDataBase.userDAO().getAuthenticatedUserByLogin(user.login) ?: return false

        val profileEntity = com.example.androidlabs.Storage.Entities
                .Profile(newUser.id)

        appDataBase.profileDAO().saveProfile(profileEntity)

        return true
    }

    override fun authenticate(login: String, password: String) : Boolean {
        val user = appDataBase.userDAO().getAuthenticatedUserByLogin(login) ?: return false

        if (BCrypt.hashpw(password, user.password) != user.password) return false

        user.isCurrent = true
        appDataBase.userDAO().saveUser(user)

        return true
    }

    override fun saveProfile(profile: Profile) {
        val oldProfile = appDataBase.profileDAO().getProfile() ?: return

        oldProfile.name = profile.name
        oldProfile.surname = profile.surname
        oldProfile.phone = profile.phone
        oldProfile.email = profile.email

        appDataBase.profileDAO().saveProfile(oldProfile)
    }

    override fun getProfile(): Profile? {
        val profileEntity = appDataBase.profileDAO().getProfile() ?: return null
        return Profile(profileEntity)
    }

    override fun savePhoto(photoPath: String) {
        val profileEntity = appDataBase.profileDAO().getProfile() ?: return
        profileEntity.imagePath = photoPath
        appDataBase.profileDAO().saveProfile(profileEntity)
    }
}