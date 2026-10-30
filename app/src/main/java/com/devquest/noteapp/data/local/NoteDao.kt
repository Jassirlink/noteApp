package com.devquest.noteapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * NoteDao - Data Access Object
 *
 * CONCEPT: Room DAO
 * -----------------
 * @Dao tells Room: "This interface defines how to interact with the database."
 * Room generates the actual implementation code at compile time (via KSP).
 *
 * You write the WHAT (SQL queries), Room writes the HOW (implementation).
 *
 * CONCEPT: Flow in DAO
 * --------------------
 * @Query that returns Flow<List<NoteEntity>> is a "live query."
 * Every time the notes table changes, Room automatically emits a new list.
 * This powers the "reactive" behavior: UI updates automatically!
 *
 * CONCEPT: suspend functions
 * --------------------------
 * suspend means the function is a coroutine — it runs on a background thread
 * and doesn't block the main (UI) thread.
 */
@Dao
interface NoteDao {

    /**
     * Gets ALL notes ordered by timestamp (newest first).
     * Returns a Flow — the UI will auto-update when notes change.
     */
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    /**
     * Gets a single note by ID. Returns null if the note doesn't exist.
     */
    @Query("SELECT * FROM notes WHERE id = :id")
    suspend fun getNoteById(id: Int): NoteEntity?

    /**
     * Inserts a note. If a note with the same ID exists, it replaces it.
     * OnConflictStrategy.REPLACE handles conflicts automatically.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    /**
     * Updates an existing note. Room matches by primary key (id).
     */
    @Update
    suspend fun updateNote(note: NoteEntity)

    /**
     * Deletes a note. Room matches by primary key (id).
     */
    @Delete
    suspend fun deleteNote(note: NoteEntity)
}

