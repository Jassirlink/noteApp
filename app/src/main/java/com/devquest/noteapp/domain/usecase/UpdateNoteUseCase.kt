package com.devquest.noteapp.domain.usecase

import com.devquest.noteapp.domain.model.Note
import com.devquest.noteapp.domain.repository.NoteRepository
import javax.inject.Inject

/**
 * UpdateNoteUseCase - Business logic for updating an existing note.
 *
 * Same validation as AddNoteUseCase: title must not be blank.
 * Also updates the timestamp to "now" so edited notes show current time.
 */
class UpdateNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw IllegalArgumentException("Note title cannot be empty")
        }
        // Update the timestamp whenever a note is edited
        repository.updateNote(note.copy(timestamp = System.currentTimeMillis()))
    }
}

