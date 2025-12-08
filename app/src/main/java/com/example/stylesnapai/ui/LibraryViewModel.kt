package com.example.stylesnapai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stylesnapai.data.repository.LibraryRepository
import com.example.stylesnapai.model.SavedImage
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LibraryViewModel(private val libraryRepository: LibraryRepository) : ViewModel() {
    val savedImages: StateFlow<List<SavedImage>> = libraryRepository.savedImages
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun save(image: SavedImage) {
        viewModelScope.launch {
            libraryRepository.saveImage(image.uri, image.styleLabel, image.originalUri)
        }
    }

    fun save(uri: android.net.Uri, label: String, originalUri: android.net.Uri?) {
        viewModelScope.launch {
            libraryRepository.saveImage(uri, label, originalUri)
        }
    }

    fun delete(image: SavedImage) {
        viewModelScope.launch {
            libraryRepository.deleteImage(image)
        }
    }
}
