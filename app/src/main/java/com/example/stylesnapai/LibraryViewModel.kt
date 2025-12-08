package com.example.stylesnapai

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesnapai.model.SavedImage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as StyleSnapApplication).libraryRepository

    val library: StateFlow<List<SavedImage>> = repository.savedImages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveImage(uri: Uri, style: String, original: Uri?) {
        viewModelScope.launch { repository.saveImage(uri, style, original) }
    }

    fun deleteImage(image: SavedImage) {
        viewModelScope.launch { repository.deleteImage(image) }
    }

    fun findEntryByUri(uri: String): SavedImage? = library.value.firstOrNull { it.uri.toString() == uri }
}
