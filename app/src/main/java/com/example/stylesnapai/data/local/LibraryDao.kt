package com.example.stylesnapai.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {
    @Query("SELECT * FROM library_images ORDER BY createdAt DESC")
    fun getAllImages(): Flow<List<LibraryImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(image: LibraryImageEntity): Long

    @Delete
    suspend fun delete(image: LibraryImageEntity)

    @Query("SELECT * FROM custom_styles ORDER BY id DESC")
    fun getCustomStyles(): Flow<List<CustomStyleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStyle(style: CustomStyleEntity): Long

    @Delete
    suspend fun deleteStyle(style: CustomStyleEntity)
}
