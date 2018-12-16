package com.example.androidlabs

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.androidlabs.Models.User
import com.example.androidlabs.Protocols.IAboutService
import com.example.androidlabs.Protocols.ISignIn
import com.example.androidlabs.Protocols.ISignUp
import com.example.androidlabs.Services.AboutService
import com.example.androidlabs.Storage.AppDatabase
import com.example.androidlabs.Storage.IStorage
import com.example.androidlabs.Storage.SQLStorage

class AuthenticationActivity : AppCompatActivity(), IAboutService, ISignIn, ISignUp {

    private val aboutService = AboutService(this)
    private lateinit var storage : IStorage
    private lateinit var navigationController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        navigationController = Navigation.findNavController(this, R.id.auth_activity_fragment)
        setupActionBar(navigationController)

        val database = AppDatabase.getDatabase(this)
        storage = SQLStorage(database)
    }

    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val navController = Navigation.findNavController(this, R.id.auth_activity_fragment)

        val navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.auth_activity_fragment).navigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            AboutService.requestPermissionPhoneState -> {
                if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    aboutService.getImei()
                }

                else if ((grantResults.isNotEmpty() &&
                                grantResults[0] == PackageManager.PERMISSION_DENIED)) {
                    showPermissionExplanation(Manifest.permission.READ_PHONE_STATE,
                            getString(R.string.read_phone_state_permission_explanation),
                            AboutService.requestPermissionPhoneState)
                }
                return
            }
        }
    }

    private fun showPermissionExplanation (permission : String, explanation : String,
                                           permissionRequestCode: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            val builder = AlertDialog.Builder(this)
            val dialogQuestion = getString(R.string.permission_explanation_dialog_question)
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

    override fun getImei() : String? {
        return aboutService.getImei()
    }

    override fun getVersion(): String {
        return aboutService.getVersion()
    }

    override fun signIn(login : String, password: String) : Boolean {
        val isAuthenticated = storage.authenticate(login, password)
        if (isAuthenticated) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        return isAuthenticated
    }

    override fun signUp(login: String, password: String) : Boolean {
        val user = User(login, password, false)
        val isUserCreated = storage.createUser(user)
        if (isUserCreated)
            navigationController.navigate(R.id.destination_sign_in)
        return isUserCreated
    }

}