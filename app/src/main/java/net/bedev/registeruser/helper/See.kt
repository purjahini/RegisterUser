package net.bedev.registeruser.helper

import android.content.Context
import android.util.Log
import android.widget.Toast
import java.math.BigInteger
import java.security.MessageDigest

class See {

    companion object {

        var TAG: String = "log_register_app"

        fun log(message: String) {

            try {
                Log.d(TAG, message)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun String.md5(): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
        }


        fun log(key: String, message: String) {
            try {
                Log.d(TAG, key + " -> " + message)
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }

        fun logE(message: String) {
            try {
                Log.e(TAG, message)
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }


        fun logE(key: String, message: String) {
            try {
                Log.e(TAG, key + " -> " + message)
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }


        fun toast(context: Context, message: String) {
            try {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }
}
