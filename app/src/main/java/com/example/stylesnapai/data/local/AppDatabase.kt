package com.example.stylesnapai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [SavedImageEntity::class, CustomStyleEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun savedImageDao(): SavedImageDao
    abstract fun customStyleDao(): CustomStyleDao
}
