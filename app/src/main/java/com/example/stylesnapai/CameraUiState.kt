package com.example.stylesnapai

import android.net.Uri
import com.example.stylesnapai.data.model.StyledImage

data class CameraUiState(
    val isProcessing: Boolean = false,
    val error: String? = null,
    val generatedImages: List<StyledImage> = emptyList(),
    val originalUri: Uri? = null
)
