package com.example.stylesnapai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.stylesnapai.ui.CameraViewModel
import com.example.stylesnapai.ui.LibraryViewModel
import com.example.stylesnapai.ui.Screen
import com.example.stylesnapai.ui.SettingsViewModel
import com.example.stylesnapai.ui.screens.CameraScreen
import com.example.stylesnapai.ui.screens.DetailScreen
import com.example.stylesnapai.ui.screens.LibraryScreen
import com.example.stylesnapai.ui.screens.ResultsScreen
import com.example.stylesnapai.ui.screens.SettingsScreen
import com.example.stylesnapai.ui.theme.StyleSnapTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as StyleSnapApplication
        setContent {
            StyleSnapTheme {
                val navController = rememberNavController()
                val cameraViewModel: CameraViewModel = viewModel(
                    factory = ViewModelFactory { CameraViewModel(app.aiImageRepository, app.styleRepository) }
                )
                val libraryViewModel: LibraryViewModel = viewModel(
                    factory = ViewModelFactory { LibraryViewModel(app.libraryRepository) }
                )
                val settingsViewModel: SettingsViewModel = viewModel(
                    factory = ViewModelFactory { SettingsViewModel(app.styleRepository) }
                )
                MainScaffold(
                    navController = navController,
                    cameraViewModel = cameraViewModel,
                    libraryViewModel = libraryViewModel,
                    settingsViewModel = settingsViewModel
                )
            }
        }
    }
}

@Composable
fun MainScaffold(
    navController: NavHostController,
    cameraViewModel: CameraViewModel,
    libraryViewModel: LibraryViewModel,
    settingsViewModel: SettingsViewModel
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            val showBottomBar = Screen.tabs.any { it.route == currentDestination?.route }
            if (showBottomBar) {
                NavigationBar {
                    Screen.tabs.forEach { screen ->
                        val selected = currentDestination?.route == screen.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                when (screen) {
                                    Screen.Camera -> Icon(Icons.Default.PhotoCamera, contentDescription = screen.label)
                                    Screen.Library -> Icon(Icons.Default.Collections, contentDescription = screen.label)
                                    Screen.Settings -> Icon(Icons.Default.Settings, contentDescription = screen.label)
                                    else -> {}
                                }
                            },
                            label = { Text(screen.label) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Camera.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Camera.route) {
                CameraScreen(navController = navController, cameraViewModel = cameraViewModel)
            }
            composable(Screen.Library.route) {
                LibraryScreen(navController = navController, viewModel = libraryViewModel)
            }
            composable(Screen.Settings.route) {
                SettingsScreen(viewModel = settingsViewModel)
            }
            composable(Screen.Results.route) {
                ResultsScreen(navController = navController, cameraViewModel = cameraViewModel)
            }
            composable(
                route = "${Screen.Detail.route}?uri={uri}&label={label}&id={id}&original={original}",
                arguments = listOf(
                    androidx.navigation.navArgument("uri") { defaultValue = "" },
                    androidx.navigation.navArgument("label") { defaultValue = "" },
                    androidx.navigation.navArgument("id") { defaultValue = -1L },
                    androidx.navigation.navArgument("original") { defaultValue = "" }
                )
            ) { backStackEntry ->
                DetailScreen(
                    navController = navController,
                    uri = backStackEntry.arguments?.getString("uri").orEmpty(),
                    label = backStackEntry.arguments?.getString("label").orEmpty(),
                    id = backStackEntry.arguments?.getLong("id") ?: -1,
                    original = backStackEntry.arguments?.getString("original"),
                    libraryViewModel = libraryViewModel
                )
            }
        }
    }
}
