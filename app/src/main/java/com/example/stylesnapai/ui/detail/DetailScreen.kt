package com.example.stylesnapai.ui.detail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.stylesnapai.LibraryViewModel
import com.example.stylesnapai.model.SavedImage

@Composable
fun DetailScreen(
    navController: NavController,
    uri: String,
    label: String,
    libraryViewModel: LibraryViewModel,
    originalUri: String?,
) {
    val context = LocalContext.current
    val library by libraryViewModel.library.collectAsStateWithLifecycle()
    val entry: SavedImage? = library.find { it.uri.toString() == uri }
    val bitmap = rememberAsyncImageBitmap(uri)

    Column(modifier = Modifier.padding(16.dp)) {
        bitmap?.let {
            Image(bitmap = it.asImageBitmap(), contentDescription = label)
        }
        Text(text = label, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(onClick = {
                libraryViewModel.saveImage(Uri.parse(uri), label, originalUri?.let { Uri.parse(it) })
            }) { Text("Save to Library") }
            Button(onClick = { shareImage(context, uri) }) { Text("Share") }
        }
        entry?.let {
            Button(onClick = { libraryViewModel.deleteImage(it); navController.popBackStack() }, modifier = Modifier.padding(top = 8.dp)) {
                Text("Delete from Library")
            }
        }
    }
}

@Composable
private fun rememberAsyncImageBitmap(uri: String): android.graphics.Bitmap? {
    return runCatching {
        val resolver = LocalContext.current.contentResolver
        resolver.openInputStream(Uri.parse(uri))?.use { input ->
            android.graphics.BitmapFactory.decodeStream(input)
        }
    }.getOrNull()
}

private fun shareImage(context: android.content.Context, uri: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, Uri.parse(uri))
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share image"))
}
