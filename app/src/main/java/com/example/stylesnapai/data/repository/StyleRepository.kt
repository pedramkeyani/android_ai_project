package com.example.stylesnapai.data.repository

import com.example.stylesnapai.data.local.CustomStyleDao
import com.example.stylesnapai.data.local.CustomStyleEntity
import com.example.stylesnapai.model.CustomStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StyleRepository(private val customStyleDao: CustomStyleDao) {

    val customStyles: Flow<List<CustomStyle>> = customStyleDao.getAll().map { entities ->
        entities.map { CustomStyle(id = it.id, name = it.name, prompt = it.prompt) }
    }

    suspend fun addStyle(name: String, prompt: String) {
        customStyleDao.insert(CustomStyleEntity(name = name, prompt = prompt))
    }

    suspend fun updateStyle(style: CustomStyle) {
        customStyleDao.update(CustomStyleEntity(id = style.id, name = style.name, prompt = style.prompt))
    }

    suspend fun deleteStyle(style: CustomStyle) {
        customStyleDao.delete(CustomStyleEntity(id = style.id, name = style.name, prompt = style.prompt))
    }
}
