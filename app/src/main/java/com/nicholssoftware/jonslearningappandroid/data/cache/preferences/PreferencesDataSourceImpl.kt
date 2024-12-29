package com.nicholssoftware.jonslearningappandroid.data.cache.preferences

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.nicholssoftware.jonslearningappandroid.domain.preferences.repository.PreferencesDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PreferencesDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesDataSource {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()


    private val encryptedPrefs = EncryptedSharedPreferences.create(
        context,
        "JonsLearningApp",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override fun isSignedIn(): Boolean = encryptedPrefs.getBoolean("hasSignedIn",  false)
    override fun setSignedIn(signedIn: Boolean) = encryptedPrefs.edit().putBoolean("hasSignedIn", signedIn).apply()
    override fun getOAuthToken(): String? = encryptedPrefs.getString("token",null)
    override fun setOAuthToken(token: String) = encryptedPrefs.edit().putString("token", token).apply()

}