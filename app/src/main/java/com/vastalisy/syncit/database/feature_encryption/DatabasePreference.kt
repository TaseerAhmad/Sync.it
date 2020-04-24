package com.vastalisy.syncit.database.feature_encryption

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class DatabasePreference(private val app: Application) {
    private var masterKeyAlias: String = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private var sharedPreferences: SharedPreferences
    private val fileName = "db_enc_key_pref"
    private val alias = "db_enc_ci_key"

    init {
        sharedPreferences = getEncryptedPreference()
        storeOneTimePassword()
    }

    fun getKey(): String? {
        return sharedPreferences.getString(alias, null)
    }

    @SuppressLint("ApplySharedPref")
    private fun storeOneTimePassword() {
        if (!sharedPreferences.contains(alias))
            sharedPreferences.edit().putString(alias, Password.getRandomPassword()).commit()
    }


    private fun getEncryptedPreference(): SharedPreferences {
        val aes256Siv = EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV
        val aes256Gcm = EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        return EncryptedSharedPreferences.create(
            fileName,
            masterKeyAlias,
            app,
            aes256Siv,
            aes256Gcm
        )
    }


}