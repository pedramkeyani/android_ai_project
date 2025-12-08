package com.example.stylesnapai.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector?, val label: String, val isTab: Boolean = true) {
    object Camera : Screen("camera", Icons.Default.PhotoCamera, "Camera")
    object Library : Screen("library", Icons.Default.Collections, "Library")
    object Settings : Screen("settings", Icons.Default.Settings, "Settings")
    object Results : Screen("results", null, "Results", isTab = false)
    object Detail : Screen("detail", null, "Detail", isTab = false)

    companion object {
        val tabs = listOf(Camera, Library, Settings)
    }
}
