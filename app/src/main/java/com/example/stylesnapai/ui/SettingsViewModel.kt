package com.example.stylesnapai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesnapai.data.repository.StyleRepository
import com.example.stylesnapai.model.CustomStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val styleRepository: StyleRepository) : ViewModel() {
    val customStyles: StateFlow<List<CustomStyle>> = styleRepository.customStyles
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _apiKey = MutableStateFlow("")
    val apiKey: StateFlow<String> = _apiKey

    fun addStyle(name: String, prompt: String) {
        viewModelScope.launch { styleRepository.addStyle(name, prompt) }
    }

    fun updateStyle(style: CustomStyle) {
        viewModelScope.launch { styleRepository.updateStyle(style) }
    }

    fun deleteStyle(style: CustomStyle) {
        viewModelScope.launch { styleRepository.deleteStyle(style) }
    }

    fun setApiKey(value: String) {
        // TODO: persist securely with EncryptedSharedPreferences or similar
        _apiKey.value = value
    }
}
