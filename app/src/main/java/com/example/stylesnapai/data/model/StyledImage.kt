package com.example.stylesnapai.data.model

import android.graphics.Bitmap
import android.net.Uri

data class StyledImage(
    val label: String,
    val bitmap: Bitmap,
    val sourceUri: Uri? = null
)
