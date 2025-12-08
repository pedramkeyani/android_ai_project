package com.example.stylesnapai.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesnapai.data.repository.AiImageRepository
import com.example.stylesnapai.data.repository.StyleRepository
import com.example.stylesnapai.model.BuiltInStyles
import com.example.stylesnapai.model.CustomStyle
import com.example.stylesnapai.model.GenerationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CameraViewModel(
    private val aiImageRepository: AiImageRepository,
    styleRepository: StyleRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState

    val customStyles: StateFlow<List<CustomStyle>> = styleRepository.customStyles
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun generateFromUri(originalUri: Uri) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessing = true, errorMessage = null)
            try {
                val styles = BuiltInStyles + customStyles.value.map { it.name }
                val result: GenerationResult = aiImageRepository.generateStyledImages(originalUri, styles)
                _uiState.value = _uiState.value.copy(
                    latestResult = result,
                    isProcessing = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isProcessing = false,
                    errorMessage = e.message ?: "Something went wrong"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun clearResult() {
        _uiState.value = _uiState.value.copy(latestResult = null)
    }
}

data class CameraUiState(
    val isProcessing: Boolean = false,
    val latestResult: GenerationResult? = null,
    val errorMessage: String? = null
)
