package com.example.stylesnapai

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesnapai.model.BuiltInStyles
import com.example.stylesnapai.data.repository.AiImageRepository
import com.example.stylesnapai.data.repository.StyleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CameraViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application as StyleSnapApplication
    private val aiRepository: AiImageRepository = app.aiImageRepository
    private val styleRepository: StyleRepository = app.styleRepository

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState

    fun processCapturedImage(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessing = true, error = null)
            try {
                val customStyles = styleRepository.customStyles.first().map { it.name }
                val styles = BuiltInStyles + customStyles
                val result = aiRepository.generateStyledImages(uri, styles)
                _uiState.value = _uiState.value.copy(latestResult = result, isProcessing = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isProcessing = false, error = e.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
