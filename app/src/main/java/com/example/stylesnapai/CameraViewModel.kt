package com.example.stylesnapai

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesnapai.data.model.CustomStyle
import com.example.stylesnapai.data.repository.AiImageRepository
import com.example.stylesnapai.data.repository.LibraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CameraViewModel(application: Application) : AndroidViewModel(application) {
    private val aiRepository = AiImageRepository(application)
    private val libraryRepository = LibraryRepository(application)

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState

    fun processCapturedImage(uri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessing = true, error = null, originalUri = uri)
            try {
                val customStyles: List<CustomStyle> = libraryRepository.getCustomStyles().first()
                val results = aiRepository.generateStyledImages(uri, customStyles)
                _uiState.value = _uiState.value.copy(generatedImages = results, isProcessing = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isProcessing = false, error = e.message)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
