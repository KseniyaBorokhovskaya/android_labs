package com.example.androidlabs.Services

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import com.example.androidlabs.AuthenticationActivity
import com.example.androidlabs.Models.Profile
import com.example.androidlabs.Storage.AppDatabase
import com.example.androidlabs.Storage.IStorage
import com.example.androidlabs.Storage.SQLStorage
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileService(private val activity: Activity, private val storage: IStorage = SQLStorage(AppDatabase.getDatabase(activity))) {

    companion object {
        const val requestPermissionCamera = 2
        const val requestPermissionGallery = 3
        const val requestOpenCamera = 4
        const val requestOpenGallery = 5
    }

    private var photoPath = ""

    fun askPermissionForUsingCamera(){
        if (askForPermission(Manifest.permission.CAMERA, requestPermissionCamera))
            uploadPhotoFromCamera()
    }

    fun uploadPhotoFromCamera(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(activity.packageManager)?.also { _ ->
                val photoFile: File? = try {
                    getPhotoFile()
                } catch (ex: IOException) {
                    null
                }

                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                            activity,
                            "com.example.android.fileprovider",
                            it
                    )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    activity.startActivityForResult(takePictureIntent,
                            requestOpenCamera)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun getPhotoFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                timeStamp,
                ".jpg",
                storageDir
        ).apply {
            photoPath = absolutePath
        }
    }

    fun askPermissionForUsingGallery(){
        if (askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, requestPermissionGallery))
            uploadPhotoFromGallery()
    }

    fun uploadPhotoFromGallery(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(gallery, requestOpenGallery)
    }

    private fun askForPermission(manifestPermissionId: String, permissionId: Int): Boolean{

        val permission = ActivityCompat.checkSelfPermission(activity, manifestPermissionId)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    arrayOf(manifestPermissionId), permissionId)
            return false
        }
        return true
    }

    fun updatePhoto(givenPhotoPath : Uri? = null) {
        val path = if (givenPhotoPath == null) photoPath
        else getPhotoPath(givenPhotoPath)
        storage.savePhoto(path)
    }

    private fun getPhotoPath(uri: Uri) : String {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

        val cursor = activity.contentResolver.query(uri,
                filePathColumn, null, null, null)
        cursor!!.moveToFirst()

        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
        val picturePath = cursor.getString(columnIndex)
        cursor.close()

        return picturePath
    }

    fun getProfile(): Profile {
        return storage.getProfile() ?: Profile()
    }

    fun saveProfileInfo(profile: Profile) {
        storage.saveProfile(profile)
    }

    fun signOut() {
        storage.signOut()
        activity.startActivity(Intent(activity, AuthenticationActivity::class.java))
    }
}