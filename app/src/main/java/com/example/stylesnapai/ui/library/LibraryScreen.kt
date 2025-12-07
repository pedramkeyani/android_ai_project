package com.example.stylesnapai.ui.library

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.icons.Icons
import androidx.compose.material3.icons.filled.Delete
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.stylesnapai.LibraryViewModel
import com.example.stylesnapai.navigation.NavRoutes

@Composable
fun LibraryScreen(navController: NavController, viewModel: LibraryViewModel) {
    val libraryItems by viewModel.library.collectAsStateWithLifecycle()
    LazyVerticalGrid(columns = GridCells.Adaptive(140.dp), contentPadding = PaddingValues(12.dp)) {
        items(libraryItems) { item ->
            Card(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    navController.navigate(NavRoutes.detailRoute(Uri.encode(item.fileUri), Uri.encode(item.styleLabel)))
                }
            ) {
                AsyncImage(
                    model = item.fileUri,
                    contentDescription = item.styleLabel,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = item.styleLabel,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
                IconButton(onClick = { viewModel.deleteImage(item) }) {
                    Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
