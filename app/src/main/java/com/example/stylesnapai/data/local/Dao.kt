package com.example.stylesnapai.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedImageDao {
    @Query("SELECT * FROM saved_images ORDER BY created_at DESC")
    fun getAll(): Flow<List<SavedImageEntity>>

    @Insert
    suspend fun insert(entity: SavedImageEntity): Long

    @Delete
    suspend fun delete(entity: SavedImageEntity)
}

@Dao
interface CustomStyleDao {
    @Query("SELECT * FROM custom_styles ORDER BY id DESC")
    fun getAll(): Flow<List<CustomStyleEntity>>

    @Insert
    suspend fun insert(entity: CustomStyleEntity): Long

    @Update
    suspend fun update(entity: CustomStyleEntity)

    @Delete
    suspend fun delete(entity: CustomStyleEntity)
}
