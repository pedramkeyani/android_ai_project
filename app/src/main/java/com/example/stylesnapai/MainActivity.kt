package com.example.stylesnapai

import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.stylesnapai.navigation.NavRoutes
import com.example.stylesnapai.ui.camera.CameraScreen
import com.example.stylesnapai.ui.detail.DetailScreen
import com.example.stylesnapai.ui.library.LibraryScreen
import com.example.stylesnapai.ui.results.ResultsScreen
import com.example.stylesnapai.ui.settings.SettingsScreen
import com.example.stylesnapai.ui.theme.StyleSnapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StyleSnapTheme {
                StyleSnapApp()
            }
        }
    }
}

@Composable
fun StyleSnapApp() {
    val navController = rememberNavController()
    val cameraViewModel: CameraViewModel = viewModel()
    val libraryViewModel: LibraryViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()

    val bottomDestinations = listOf(
        NavRoutes.CAMERA to Icons.Default.CameraAlt,
        NavRoutes.LIBRARY to Icons.Default.Collections,
        NavRoutes.SETTINGS to Icons.Default.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomDestinations.forEach { (route, icon) ->
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.route == route } == true,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(imageVector = icon, contentDescription = route) },
                        label = null
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.CAMERA,
            modifier = Modifier.padding(padding)
        ) {
            composable(NavRoutes.CAMERA) {
                CameraScreen(
                    navController = navController,
                    viewModel = cameraViewModel,
                    onThumbnailClick = { navController.navigate(NavRoutes.LIBRARY) },
                    onSettingsClick = { navController.navigate(NavRoutes.SETTINGS) }
                )
            }
            composable(NavRoutes.RESULTS) {
                ResultsScreen(
                    navController = navController,
                    viewModel = cameraViewModel,
                    onOpenDetail = { uri, label ->
                        navController.navigate(NavRoutes.detailRoute(Uri.encode(uri), Uri.encode(label)))
                    }
                )
            }
            composable(NavRoutes.LIBRARY) {
                LibraryScreen(navController = navController, viewModel = libraryViewModel)
            }
            composable(NavRoutes.SETTINGS) {
                SettingsScreen(viewModel = settingsViewModel)
            }
            composable(NavRoutes.DETAIL) { backStackEntry ->
                val uri = backStackEntry.arguments?.getString("uri")?.let { Uri.decode(it) }
                val label = backStackEntry.arguments?.getString("label")?.let { Uri.decode(it) }
                if (uri != null && label != null) {
                    DetailScreen(
                        navController = navController,
                        uri = uri,
                        label = label,
                        libraryViewModel = libraryViewModel,
                        originalUri = cameraViewModel.uiState.value.originalUri?.toString()
                    )
                }
            }
        }
    }
}
