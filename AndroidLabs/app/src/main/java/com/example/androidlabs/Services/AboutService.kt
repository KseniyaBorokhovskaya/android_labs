package com.example.androidlabs.Services

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.example.androidlabs.BuildConfig

class AboutService(private val activity: Activity) {

    companion object {
        const val requestPermissionPhoneState = 1
    }

    fun getVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    fun getImei() : String? {
        var imei: String? = null
        if (askForPermission()) {
            val telephonyManager = activity.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            imei = telephonyManager.deviceId
        }
        return imei// ?: activity.getString(R.string.no_information)
    }

    private fun askForPermission(): Boolean{

        var permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_PHONE_STATE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.READ_PHONE_STATE), requestPermissionPhoneState)
            return false
        }
        return true
    }


}