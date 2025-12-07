package com.example.stylesnapai.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.example.stylesnapai.data.local.AppDatabase
import com.example.stylesnapai.data.local.CustomStyleEntity
import com.example.stylesnapai.data.local.LibraryImageEntity
import com.example.stylesnapai.data.model.CustomStyle
import com.example.stylesnapai.data.storage.ImageStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LibraryRepository(private val context: Context) {
    private val db = AppDatabase.get(context)
    private val storage = ImageStorage(context)
    private val dao = db.libraryDao()

    fun getLibrary(): Flow<List<LibraryImageEntity>> = dao.getAllImages()

    fun getCustomStyles(): Flow<List<CustomStyle>> = dao.getCustomStyles().map { list ->
        list.map { CustomStyle(it.id, it.name, it.prompt) }
    }

    suspend fun saveStyle(name: String, prompt: String) {
        dao.insertStyle(CustomStyleEntity(name = name, prompt = prompt))
    }

    suspend fun deleteStyle(style: CustomStyle) {
        dao.deleteStyle(CustomStyleEntity(id = style.id, name = style.name, prompt = style.prompt))
    }

    suspend fun saveImage(bitmapUri: Uri, style: String, original: Uri?): Long {
        val savedUri = storage.persistImage(bitmapUri, style)
        return dao.insert(
            LibraryImageEntity(
                fileUri = savedUri.toString(),
                styleLabel = style,
                createdAt = System.currentTimeMillis(),
                originalUri = original?.toString()
            )
        )
    }

    suspend fun deleteImage(entity: LibraryImageEntity) {
        storage.delete(Uri.parse(entity.fileUri))
        dao.delete(entity)
    }
}
