package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TagDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "hex") val hex: String,
    @ColumnInfo(name = "name") val name: String
) {
    companion object {
        val DEFAULT_COLORS = listOf(
            TagDbModel(1, "#6972a0", "Home"),
            TagDbModel(2, "#3f4a85", "Family"),
            TagDbModel(3, "#a06f74", "Hotline"),
            TagDbModel(4, "#6a3c52", "Work"),
            TagDbModel(5, "#f5ce65", "School"),
        )
        val DEFAULT_COLOR = DEFAULT_COLORS[0]
    }
}
