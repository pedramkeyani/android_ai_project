package com.example.stylesnapai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_styles")
data class CustomStyleEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val prompt: String
)
