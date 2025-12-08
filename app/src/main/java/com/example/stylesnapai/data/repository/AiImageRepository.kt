package com.example.stylesnapai.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.stylesnapai.model.GenerationResult
import com.example.stylesnapai.model.StyledImageResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class AiImageRepository(private val context: Context) {

    /**
     * Fake AI processing pipeline. Replace with a call to your remote endpoint (OpenAI, etc.).
     * Parameters that would be sent: original image data, style prompt strings, and optional API key.
     */
    suspend fun generateStyledImages(
        original: Uri,
        styles: List<String>
    ): GenerationResult = withContext(Dispatchers.IO) {
        val results = styles.map { style ->
            delay(250) // Simulate network latency
            val bitmap = createStyledBitmap(style)
            val file = File(context.cacheDir, "styled_${UUID.randomUUID()}.jpg")
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out)
            }
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            StyledImageResult(styleLabel = style, uri = uri, originalUri = original)
        }
        GenerationResult(original = original, styled = results)
    }

    private fun createStyledBitmap(style: String): Bitmap {
        val size = 900
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val colors = listOf(
            Color.parseColor("#1CD1E1"),
            Color.parseColor("#FA1E9A"),
            Color.parseColor("#6750A4"),
            Color.parseColor("#0B132B"),
            Color.parseColor("#64FFDA"),
            Color.parseColor("#FF8A65"),
            Color.parseColor("#A5D6A7"),
            Color.parseColor("#90CAF9")
        )
        canvas.drawColor(colors[Math.abs(style.hashCode()) % colors.size])
        val paint = Paint().apply {
            color = Color.WHITE
            textSize = 48f
            isAntiAlias = true
        }
        canvas.drawText(style, 40f, size / 2f, paint)
        return bitmap
    }
}
