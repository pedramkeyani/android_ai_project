package com.example.stylesnapai

import android.app.Application
import androidx.room.Room
import com.example.stylesnapai.data.local.AppDatabase
import com.example.stylesnapai.data.repository.AiImageRepository
import com.example.stylesnapai.data.repository.LibraryRepository
import com.example.stylesnapai.data.repository.StyleRepository

class StyleSnapApplication : Application() {
    lateinit var database: AppDatabase
        private set

    lateinit var aiImageRepository: AiImageRepository
        private set

    lateinit var libraryRepository: LibraryRepository
        private set

    lateinit var styleRepository: StyleRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "stylesnap-db"
        ).build()
        aiImageRepository = AiImageRepository(this)
        libraryRepository = LibraryRepository(database.savedImageDao())
        styleRepository = StyleRepository(database.customStyleDao())
    }
}
