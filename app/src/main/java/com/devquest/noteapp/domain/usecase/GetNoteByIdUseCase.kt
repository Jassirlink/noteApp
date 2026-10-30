package com.devquest.noteapp.domain.usecase

import com.devquest.noteapp.domain.model.Note
import com.devquest.noteapp.domain.repository.NoteRepository
import javax.inject.Inject

/**
 * GetNoteByIdUseCase - Fetches a single note by its ID.
 *
 * Used when the user taps on a note to edit it.
 * The ViewModel passes the note's ID, and this use case fetches the full note.
 */
class GetNoteByIdUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}

