package com.example.stylesnapai.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.stylesnapai.model.SavedImage
import com.example.stylesnapai.ui.LibraryViewModel
import com.example.stylesnapai.ui.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(navController: NavController, viewModel: LibraryViewModel) {
    val images by viewModel.savedImages.collectAsState()
    Scaffold(
        topBar = { CenterAlignedTopAppBar(title = { Text("Library") }) }
    ) { padding ->
        if (images.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No images yet. Take a photo to get started.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(images) { item ->
                    LibraryItem(
                        item,
                        onOpen = {
                            navController.navigate(
                                "${Screen.Detail.route}?uri=${java.net.URLEncoder.encode(item.uri.toString(), "UTF-8")}&label=${java.net.URLEncoder.encode(item.styleLabel, "UTF-8")}&id=${item.id}&original=${java.net.URLEncoder.encode(item.originalUri?.toString() ?: "", "UTF-8")}"
                            )
                        },
                        onDelete = {
                            viewModel.delete(item)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun LibraryItem(item: SavedImage, onOpen: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable { onOpen() }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = item.uri,
                contentDescription = item.styleLabel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(item.styleLabel, style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
