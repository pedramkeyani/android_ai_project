package com.example.stylesnapai

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesnapai.data.local.LibraryImageEntity
import com.example.stylesnapai.data.repository.LibraryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = LibraryRepository(application)

    val library: StateFlow<List<LibraryImageEntity>> = repository.getLibrary()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveImage(uri: Uri, style: String, original: Uri?) {
        viewModelScope.launch { repository.saveImage(uri, style, original) }
    }

    fun deleteImage(entity: LibraryImageEntity) {
        viewModelScope.launch { repository.deleteImage(entity) }
    }

    fun findEntryByUri(uri: String): LibraryImageEntity? = library.value.firstOrNull { it.fileUri == uri }
}
