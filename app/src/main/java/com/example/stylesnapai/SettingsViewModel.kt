package com.example.stylesnapai

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesnapai.model.CustomStyle
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as StyleSnapApplication).styleRepository

    val customStyles: StateFlow<List<CustomStyle>> = repository.customStyles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addStyle(name: String, prompt: String) {
        viewModelScope.launch { repository.addStyle(name, prompt) }
    }

    fun deleteStyle(style: CustomStyle) {
        viewModelScope.launch { repository.deleteStyle(style) }
    }
}
