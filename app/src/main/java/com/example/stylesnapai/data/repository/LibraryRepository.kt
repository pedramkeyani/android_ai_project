package com.example.stylesnapai.data.repository

import android.net.Uri
import com.example.stylesnapai.data.local.SavedImageDao
import com.example.stylesnapai.data.local.SavedImageEntity
import com.example.stylesnapai.model.SavedImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LibraryRepository(private val savedImageDao: SavedImageDao) {

    val savedImages: Flow<List<SavedImage>> = savedImageDao.getAll().map { entities ->
        entities.map { entity ->
            SavedImage(
                id = entity.id,
                uri = Uri.parse(entity.uri),
                styleLabel = entity.styleLabel,
                createdAt = entity.createdAt,
                originalUri = entity.originalUri?.let { Uri.parse(it) }
            )
        }
    }

    suspend fun saveImage(uri: Uri, styleLabel: String, originalUri: Uri?): Long {
        val entity = SavedImageEntity(
            uri = uri.toString(),
            styleLabel = styleLabel,
            createdAt = System.currentTimeMillis(),
            originalUri = originalUri?.toString()
        )
        return savedImageDao.insert(entity)
    }

    suspend fun deleteImage(image: SavedImage) {
        val entity = SavedImageEntity(
            id = image.id,
            uri = image.uri.toString(),
            styleLabel = image.styleLabel,
            createdAt = image.createdAt,
            originalUri = image.originalUri?.toString()
        )
        savedImageDao.delete(entity)
    }
}
