package com.example.androidlabs

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.appcompat.app.AlertDialog
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.androidlabs.Models.Profile
import com.example.androidlabs.Protocols.IAboutService
import com.example.androidlabs.Protocols.IProfileService
import com.example.androidlabs.Services.AboutService
import com.example.androidlabs.Services.ProfileService
import com.example.androidlabs.Storage.AppDatabase
import com.example.androidlabs.Storage.IStorage
import com.example.androidlabs.Storage.SQLStorage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IAboutService, IProfileService {

    private val aboutService = AboutService(this)
    private lateinit var profileService : ProfileService
   // private lateinit var storage : IStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // addVersionToView()
        // addPhoneImeiToLayout()

        //storage = SQLStorage(AppDatabase.getDatabase(this))
        profileService = ProfileService(this)//, storage)

        val navigationController = Navigation.findNavController(this, R.id.main_activity_fragment)
        addBottomNavigation(navigationController)
        setupActionBar(navigationController)
    }

    private fun addBottomNavigation(navController: NavController) {
        bottom_navigation_view?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val navController = Navigation.findNavController(this, R.id.main_activity_fragment)
        val navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.main_activity_fragment).navigateUp()
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            AboutService.requestPermissionPhoneState -> {
//                if ((grantResults.isNotEmpty() &&
//                                grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                }
//                else
                if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_DENIED)) {
                    showPermissionExplanation(Manifest.permission.READ_PHONE_STATE,
                            getString(R.string.read_phone_state_permission_explanation),
                            AboutService.requestPermissionPhoneState)
                }
                return
            }

            ProfileService.requestPermissionGallery -> {
                if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    profileService.uploadPhotoFromGallery()
                }
                else if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_DENIED)) {
                    showPermissionExplanation(Manifest.permission.READ_EXTERNAL_STORAGE,
                            getString(R.string.read_phone_state_permission_explanation),
                            ProfileService.requestPermissionGallery)
                }
                return
            }
            ProfileService.requestPermissionCamera -> {
                if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    profileService.uploadPhotoFromCamera()
                }
                else if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_DENIED)) {
                    showPermissionExplanation(Manifest.permission.CAMERA,
                            getString(R.string.read_phone_state_permission_explanation),
                            ProfileService.requestPermissionCamera)
                }
                return
            }
        }
    }

    private fun showPermissionExplanation (permission : String, explanation : String,
                                           permissionRequestCode: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            val builder = AlertDialog.Builder(this)
            var dialogQuestion = getString(R.string.permission_explanation_dialog_question)
            builder.setMessage("$explanation $dialogQuestion")
                    .setTitle(R.string.permission_explanation_dialog_title)

            builder.setPositiveButton("@string/yes" ){ _, _ ->
            }
                    .setNegativeButton("@string/no" ) { _, _ ->
                        ActivityCompat.requestPermissions(this,
                                arrayOf(permission), permissionRequestCode)

                    }

            builder.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null) return
        when (requestCode) {
            ProfileService.requestOpenGallery -> {
                val selectedImageURI = data.data ?: return

                profileService.updatePhoto(selectedImageURI)
            }
            ProfileService.requestOpenCamera -> profileService.updatePhoto()
        }
    }

    override fun getImei(): String? {
        return aboutService.getImei()
    }

    override fun getVersion(): String {
        return aboutService.getVersion()
    }

    override fun askPermissionForUsingCamera() {
        return profileService.askPermissionForUsingCamera()
    }

    override fun askPermissionForUsingGallery() {
        return profileService.askPermissionForUsingGallery()
    }

    override fun getProfile(): Profile {
        return profileService.getProfile()
    }

    override fun saveProfileInfo(profile: Profile) {
        profileService.saveProfileInfo(profile)
    }

    override fun signOut() {
        profileService.signOut()
    }
}
