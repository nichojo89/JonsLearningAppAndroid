package com.nicholssoftware.jonslearningappandroid.domain.preferences.repository

interface PreferencesDataSource {
    fun isSignedIn(): Boolean
    fun setSignedIn(signedIn: Boolean)
    fun getOAuthToken(): String?
    fun setOAuthToken(token: String)
}