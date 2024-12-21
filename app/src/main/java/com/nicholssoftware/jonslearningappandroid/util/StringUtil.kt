package com.nicholssoftware.jonslearningappandroid.util

fun String.isValidEmail() : Boolean {
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
    return emailRegex.matches(this)
}