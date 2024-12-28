package com.nicholssoftware.jonslearningappandroid.ui.characters.character_gen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CharacterGeneratorViewModel @Inject constructor() : ViewModel(){
    private val _prompt = mutableStateOf("Cyberpunk warrior, futuristic, sci fi, bearded man")
    val prompt: State<String> = _prompt

    private val _title = mutableStateOf("Describe character")
    val title: State<String> = _title

    private val _generateCharacterEnabled = mutableStateOf(true)
    val generateCharacterEnabled: State<Boolean> = _generateCharacterEnabled


    fun updatePrompt(prompt: String){
        _prompt.value = prompt
    }

    fun generateCharacter(){

    }
}