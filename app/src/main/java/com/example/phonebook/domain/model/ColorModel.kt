package com.example.phonebook.domain.model

import com.example.phonebook.database.TagDbModel

data class ColorModel(
    val id: Long,
    val name: String,
    val hex: String
) {
    companion object {
        val DEFAULT = with(TagDbModel.DEFAULT_COLOR) { ColorModel(id, name, hex) }
    }
}
