package com.nicholssoftware.jonslearningappandroid.domain.preferences

interface PreferencesDataSource {
    fun isSignedIn(): Boolean
    fun setSignedIn(signedIn: Boolean)
    fun getOAuthToken(): String?
    fun setOAuthToken(token: String)
}