package com.nicholssoftware.jonslearningappandroid.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel : ViewModel() {
    private val _navigationEvent = MutableStateFlow<String?>(null)
    val navigationEvent: StateFlow<String?> = _navigationEvent

    fun updateNavigationEvent(event: String){
        _navigationEvent.value = event
    }
    fun resetNavigation() {
        _navigationEvent.value = null
    }
}