package com.example.stylesnapai.data.storage

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImageStorage(private val context: Context) {

    fun persistImage(source: Uri, label: String): Uri {
        val resolver = context.contentResolver
        val input = resolver.openInputStream(source) ?: return source
        val fileName = "stylesnap_${label}_${System.currentTimeMillis()}.jpg"
        val savedUri: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/StyleSnapAI")
            }
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?: return source
        } else {
            val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "StyleSnapAI")
            dir.mkdirs()
            val file = File(dir, fileName)
            Uri.fromFile(file)
        }

        resolver.openOutputStream(savedUri)?.use { output ->
            input.use { inp -> inp.copyTo(output) }
        }
        return savedUri
    }

    fun delete(uri: Uri) {
        runCatching { context.contentResolver.delete(uri, null, null) }
    }
}
