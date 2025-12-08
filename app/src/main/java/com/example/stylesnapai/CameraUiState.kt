package com.example.stylesnapai

import com.example.stylesnapai.model.GenerationResult

// UI state for the legacy camera flow backed by GenerationResult data
// produced by AiImageRepository.
data class CameraUiState(
    val isProcessing: Boolean = false,
    val error: String? = null,
    val latestResult: GenerationResult? = null
)
