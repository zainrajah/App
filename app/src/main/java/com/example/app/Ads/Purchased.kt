package com.example.app
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.View

object Purchased {
    fun checkPurchased(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val purchased = sharedPreferences.getBoolean("purchased", false)
        if (purchased) {
            return true
        } else {
            return false
        }

    }
}