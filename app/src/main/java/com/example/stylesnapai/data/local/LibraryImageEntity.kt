package com.example.stylesnapai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "library_images")
data class LibraryImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fileUri: String,
    val styleLabel: String,
    val createdAt: Long,
    val originalUri: String? = null
)
