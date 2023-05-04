package com.example.phonebook.database

import com.example.phonebook.domain.model.ColorModel
import com.example.phonebook.domain.model.NEW_NOTE_ID
import com.example.phonebook.domain.model.NoteModel

class DbMapper {
    // Create list of NoteModels by pairing each note with a color
    fun mapNotes(
        contactDbModels: List<ContactDbModel>,
        tagDbModels: Map<Long, TagDbModel>
    ): List<NoteModel> = contactDbModels.map {
        val colorDbModel = tagDbModels[it.tagId]
            ?: throw RuntimeException("Color for colorId: ${it.tagId} was not found. Make sure that all colors are passed to this method")

        mapNote(it, colorDbModel)
    }

    // convert NoteDbModel to NoteModel
    fun mapNote(contactDbModel: ContactDbModel, tagDbModel: TagDbModel): NoteModel {
        val color = mapColor(tagDbModel)
        val isCheckedOff = with(contactDbModel) { if (canBeCheckedOff) isCheckedOff else false }
        return with(contactDbModel) { NoteModel(id, title, content, isCheckedOff, color) }
    }

    // convert list of ColorDdModels to list of ColorModels
    fun mapColors(tagDbModels: List<TagDbModel>): List<ColorModel> =
        tagDbModels.map { mapColor(it) }

    // convert ColorDbModel to ColorModel
    fun mapColor(tagDbModel: TagDbModel): ColorModel =
        with(tagDbModel) { ColorModel(id, name, hex) }

    // convert NoteModel back to NoteDbModel
    fun mapDbNote(note: NoteModel): ContactDbModel =
        with(note) {
            val canBeCheckedOff = isCheckedOff != null
            val isCheckedOff = isCheckedOff ?: false
            if (id == NEW_NOTE_ID)
                ContactDbModel(
                    title = title,
                    content = content,
                    canBeCheckedOff = canBeCheckedOff,
                    isCheckedOff = isCheckedOff,
                    tagId = color.id,
                    isInTrash = false
                )
            else
                ContactDbModel(id, title, content, canBeCheckedOff, isCheckedOff, color.id, false)
        }
}