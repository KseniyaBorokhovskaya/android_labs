package com.example.androidlabs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.telephony.TelephonyManager
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private val _requestPermissionPhoneState = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addVersionToView()
        addPhoneImeiToLayout()
    }

    private fun addVersionToView() {
        val version = BuildConfig.VERSION_NAME
        val view = findViewById<TextView>(R.id.version)
        view.text = version
    }

    private fun getIMEI() : String {
        val telManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        var IMEI = telManager.deviceId
        return IMEI?.let{ IMEI } ?: "Don't have iformation"
    }

    private fun addPhoneImeiToLayout(){

        var permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_PHONE_STATE), _requestPermissionPhoneState)
            return
        }

        var IMEI = getIMEI()
        val phoneImeiTextView = findViewById<TextView>(R.id.IMEI)
        phoneImeiTextView.text = IMEI
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == _requestPermissionPhoneState) {
            var imei = "Don't have iformation"
            if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                imei = getIMEI()
            }
            else if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_DENIED)) {
                    showExplanationOfPermission(Manifest.permission.READ_PHONE_STATE,
                            getString(R.string.permission_explanation),
                            _requestPermissionPhoneState)
            }
                val phoneImeiTextView = findViewById<TextView>(R.id.IMEI)
                phoneImeiTextView.text = imei
                return
        }
    }

    private fun showExplanationOfPermission (permission : String, explanation : String,
                                             permissionRequestCode: Int) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            val builder = AlertDialog.Builder(this)
            var dialogQuestion = getString(R.string.permission_dialog_question)
            builder.setMessage("$explanation $dialogQuestion")
                    .setTitle(R.string.permission_dialog_title)

            builder.setPositiveButton("Yes"){ _, _ ->
            }
                    .setNegativeButton("No") { _, _ ->
                        ActivityCompat.requestPermissions(this,
                                arrayOf(permission), permissionRequestCode)

                    }

            builder.show()
        }
    }
}
