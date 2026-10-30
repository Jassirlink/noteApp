package com.devquest.noteapp.domain.usecase

import com.devquest.noteapp.domain.model.Note
import com.devquest.noteapp.domain.repository.NoteRepository
import javax.inject.Inject

/**
 * AddNoteUseCase - Business logic for adding a new note.
 *
 * Business rule enforced here: A note's title must not be blank.
 * This validation lives in the domain layer, NOT in the UI or database.
 *
 * If we add more rules later (e.g., max title length), we only change this file.
 */
class AddNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    /**
     * @throws IllegalArgumentException if the title is blank
     */
    suspend operator fun invoke(note: Note) {
        // Business rule: title cannot be empty
        if (note.title.isBlank()) {
            throw IllegalArgumentException("Note title cannot be empty")
        }
        repository.insertNote(note)
    }
}

