package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "can_be_checked_off") val canBeCheckedOff: Boolean,
    @ColumnInfo(name = "is_checked_off") val isCheckedOff: Boolean,
    @ColumnInfo(name = "color_id") val tagId: Long,
    @ColumnInfo(name = "in_trash") val isInTrash: Boolean
) {
    companion object {
        val DEFAULT_NOTES = listOf(
            ContactDbModel(1, "Hirashi", "0648464152", false, false, 1, false),
            ContactDbModel(2, "December", "0983290013", false, false, 2, false),
            ContactDbModel(3, "Pancake's shop", "0819268430", false, false, 3, false),
            ContactDbModel(4, "God", "0891449139", false, false, 4, false),
            ContactDbModel(5, "My bro", "0651334872", false, false, 5, false),
            ContactDbModel(6, "Do not call", "0896752556", false, false, 2, false),
            ContactDbModel(7, "Lil bro", "0644803102", false, false, 5, false),
            ContactDbModel(8, "Who am I", "0614924514", false, false, 1, false),
            ContactDbModel(9, "May", "0684953218", false, false, 2, false),
            ContactDbModel(10, "June", "0815524917", false, false, 5, false),
            ContactDbModel(11, "July", "0976481546", true, false, 4, false),
            ContactDbModel(12, "DADA", "0864762914", true, false, 6, false)
        )
    }
}
