package com.devquest.noteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * NoteDatabase - The Room Database
 *
 * CONCEPT: Room Database
 * ----------------------
 * @Database annotation tells Room:
 * - entities: which tables (NoteEntity) exist in this database
 * - version: the schema version (increment this when you change the schema)
 * - exportSchema: whether to save the schema to a file (false for simplicity)
 *
 * This is an abstract class — Room generates the real implementation at compile time.
 *
 * We access this from the DI module (DatabaseModule.kt), not directly.
 * The DatabaseModule provides the singleton instance to whoever needs it.
 */
@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {

    /**
     * Room generates the implementation of this function.
     * Anyone who has a NoteDatabase can get a NoteDao from here.
     */
    abstract fun noteDao(): NoteDao

    companion object {
        // The name of the database file stored on the device
        const val DATABASE_NAME = "note_database"
    }
}

