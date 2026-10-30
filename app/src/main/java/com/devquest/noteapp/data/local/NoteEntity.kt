package com.devquest.noteapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devquest.noteapp.domain.model.Note

/**
 * NoteEntity - The Room Database Entity (Data Layer)
 *
 * CONCEPT: Room Entity
 * --------------------
 * @Entity tells Room: "Create a table called 'notes' in the database."
 * Each property becomes a column in the table.
 *
 * WHY SEPARATE FROM Note (domain model)?
 * The NoteEntity is tied to Room (Android dependency).
 * The domain Note is pure Kotlin.
 * Keeping them separate means:
 * - We can add Room-specific columns (like indices for performance) without polluting domain
 * - We can switch databases without touching domain logic
 *
 * MAPPING (Mapper functions):
 * We convert between NoteEntity ↔ Note in this file.
 */
@Entity(tableName = "notes")
data class NoteEntity(

    @PrimaryKey(autoGenerate = true) // Room auto-generates unique IDs
    val id: Int = 0,

    val title: String,
    val content: String,
    val timestamp: Long
) {
    /**
     * Converts this NoteEntity (database model) to a Note (domain model).
     * Called when reading from the database.
     */
    fun toDomain(): Note {
        return Note(
            id = id,
            title = title,
            content = content,
            timestamp = timestamp
        )
    }
}

/**
 * Extension function to convert a Note (domain model) to NoteEntity (database model).
 * Called when writing to the database.
 *
 * Note: id defaults to 0 for new notes (Room auto-generates the real ID).
 */
fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id ?: 0,
        title = title,
        content = content,
        timestamp = timestamp
    )
}

