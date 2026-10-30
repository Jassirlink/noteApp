package com.devquest.noteapp.data.repository

import com.devquest.noteapp.data.local.NoteDao
import com.devquest.noteapp.data.local.toEntity
import com.devquest.noteapp.domain.model.Note
import com.devquest.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * NoteRepositoryImpl - The concrete implementation of NoteRepository.
 *
 * CONCEPT: Repository Pattern
 * ----------------------------
 * The domain layer only knows about NoteRepository (the interface).
 * This class is the ACTUAL implementation that talks to Room (the database).
 *
 * CONCEPT: Dependency Inversion in action
 * ----------------------------------------
 * Domain says: "I need a NoteRepository"
 * Data layer says: "I'll give you NoteRepositoryImpl (which uses Room)"
 * Hilt wires this up automatically via AppModule.kt
 *
 * This class:
 * 1. Receives NoteDao (from Hilt via @Inject)
 * 2. Calls DAO methods
 * 3. Maps between NoteEntity (database) ↔ Note (domain)
 */
class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
) : NoteRepository {

    /**
     * Maps the Flow of NoteEntity list to a Flow of Note list.
     * .map { } transforms each emission from the Flow.
     * Each NoteEntity is converted to a Note using .toDomain().
     */
    override fun getAllNotes(): Flow<List<Note>> {
        return dao.getAllNotes().map { entityList ->
            entityList.map { entity -> entity.toDomain() }
        }
    }

    override suspend fun getNoteById(id: Int): Note? {
        // ?.toDomain() means: if the entity is not null, convert it; otherwise return null
        return dao.getNoteById(id)?.toDomain()
    }

    override suspend fun insertNote(note: Note) {
        // Convert domain Note to NoteEntity before inserting into Room
        dao.insertNote(note.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        dao.updateNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note.toEntity())
    }
}

