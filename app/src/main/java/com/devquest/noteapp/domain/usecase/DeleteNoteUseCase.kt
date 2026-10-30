package com.devquest.noteapp.domain.usecase

import com.devquest.noteapp.domain.model.Note
import com.devquest.noteapp.domain.repository.NoteRepository
import javax.inject.Inject

/**
 * DeleteNoteUseCase - Deletes a note from the database.
 *
 * Simple use case with no extra business logic.
 * Even simple operations go through a use case to keep the architecture consistent.
 */
class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.deleteNote(note)
    }
}

