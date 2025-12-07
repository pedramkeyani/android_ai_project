package com.example.stylesnapai.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import com.example.stylesnapai.data.model.CustomStyle
import com.example.stylesnapai.data.model.StyledImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

class AiImageRepository(private val context: Context) {

    private val baseStyles = listOf(
        "Cartoon",
        "Studio Ghibli-style illustration",
        "Futuristic neon cyberpunk",
        "Vintage black-and-white photograph",
        "Oil painting",
        "Watercolor",
        "Pixel art",
        "Retro 80s film photo"
    )

    suspend fun generateStyledImages(original: Uri, customStyles: List<CustomStyle>): List<StyledImage> =
        withContext(Dispatchers.Default) {
            // TODO: Replace this mock implementation with a real OpenAI Images API call.
            // This is where you would send the original image bytes and style prompts
            // (including customStyles prompts) to the backend and receive generated images.
            delay(1500)
            val prompts = baseStyles + customStyles.map { it.name }
            prompts.map { label ->
                StyledImage(
                    label = label,
                    bitmap = renderPlaceholderBitmap(label),
                    sourceUri = original
                )
            }
        }

    private fun renderPlaceholderBitmap(label: String): Bitmap {
        val size = 640
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint().apply {
            color = Color.rgb(Random.nextInt(255), Random.nextInt(255), Random.nextInt(255))
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), paint)
        paint.color = Color.WHITE
        paint.textSize = 36f
        canvas.drawText(label.take(18), 40f, size / 2f, paint)
        return bitmap
    }
}
