package com.devquest.noteapp.domain.repository

import com.devquest.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * NoteRepository - The Repository Interface (Contract / Abstraction)
 *
 * CONCEPT: Dependency Inversion Principle (from SOLID)
 * -----------------------------------------------------
 * The domain layer defines WHAT it needs (this interface),
 * but does NOT know HOW it's implemented (Room, network, etc.).
 *
 * This is the "contract" between the domain layer and the data layer.
 *
 * CONCEPT: Flow
 * -------------
 * Flow is a Kotlin Coroutines type that emits multiple values over time.
 * `Flow<List<Note>>` means: "a stream that keeps emitting an updated list
 * of notes whenever the database changes."
 *
 * This lets our UI automatically update when data changes — no manual polling!
 */
interface NoteRepository {

    /**
     * Returns a Flow that emits the full list of notes whenever data changes.
     * The UI observes this and re-renders automatically.
     */
    fun getAllNotes(): Flow<List<Note>>

    /**
     * Returns a single note by its ID. Returns null if not found.
     * suspend = must be called from a coroutine (background thread).
     */
    suspend fun getNoteById(id: Int): Note?

    /**
     * Inserts a new note. The database auto-generates the ID.
     */
    suspend fun insertNote(note: Note)

    /**
     * Updates an existing note. The note must have a valid ID.
     */
    suspend fun updateNote(note: Note)

    /**
     * Deletes a note from the database.
     */
    suspend fun deleteNote(note: Note)
}

