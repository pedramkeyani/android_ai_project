package com.example.stylesnapai.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.stylesnapai.model.SavedImage
import com.example.stylesnapai.ui.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavController,
    uri: String,
    label: String,
    id: Long,
    original: String?,
    libraryViewModel: LibraryViewModel
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val isSaved = id != -1L
    val savedImage = remember(id, uri) {
        SavedImage(
            id = id,
            uri = android.net.Uri.parse(uri),
            styleLabel = label,
            createdAt = System.currentTimeMillis(),
            originalUri = original?.takeIf { it.isNotEmpty() }?.let { android.net.Uri.parse(it) }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(label) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = uri,
                contentDescription = label,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    libraryViewModel.save(savedImage)
                }) { Text(if (isSaved) "Save again" else "Save to Library") }

                OutlinedButton(onClick = {
                    shareImage(context, uri)
                }) { Text("Share") }
            }

            if (isSaved) {
                OutlinedButton(onClick = {
                    libraryViewModel.delete(savedImage)
                    navController.popBackStack()
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                    Spacer(Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
}

private fun shareImage(context: android.content.Context, uri: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/jpeg"
        putExtra(Intent.EXTRA_STREAM, android.net.Uri.parse(uri))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share styled image"))
}
