package com.example.stylesnapai.model

import android.net.Uri

const val BUILT_IN_STYLES_COUNT = 8

val BuiltInStyles = listOf(
    "Cartoon",
    "Studio Ghibli-style illustration",
    "Futuristic neon cyberpunk",
    "Vintage black-and-white photograph",
    "Oil painting",
    "Watercolor",
    "Pixel art",
    "Retro 80s film photo"
)

data class StyledImageResult(
    val styleLabel: String,
    val uri: Uri,
    val originalUri: Uri? = null,
    val id: Long? = null
)

data class GenerationResult(
    val original: Uri,
    val styled: List<StyledImageResult>
)

data class CustomStyle(
    val id: Long = 0,
    val name: String,
    val prompt: String
)

data class SavedImage(
    val id: Long = 0,
    val uri: Uri,
    val styleLabel: String,
    val createdAt: Long,
    val originalUri: Uri? = null
)
