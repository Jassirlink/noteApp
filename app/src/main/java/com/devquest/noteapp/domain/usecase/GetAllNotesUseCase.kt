package com.devquest.noteapp.domain.usecase

import com.devquest.noteapp.domain.model.Note
import com.devquest.noteapp.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * GetAllNotesUseCase - A Use Case for getting all notes.
 *
 * CONCEPT: Use Cases (Interactors)
 * ---------------------------------
 * A Use Case represents ONE specific action the user can do.
 * It contains the business logic for that action.
 *
 * WHY USE CASES?
 * - Single Responsibility: each class does ONE thing
 * - Reusable: multiple ViewModels can use the same use case
 * - Testable: easy to unit test in isolation
 * - The ViewModel doesn't directly talk to the repository — it goes through use cases
 *
 * CONCEPT: @Inject constructor
 * -----------------------------
 * Tells Hilt: "When someone needs GetAllNotesUseCase, create it by
 * injecting a NoteRepository into its constructor."
 * Hilt knows how to provide NoteRepository because we set it up in AppModule.
 */
class GetAllNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    /**
     * The invoke() operator lets us call this use case like a function:
     *   val notes = getAllNotesUseCase()
     * instead of:
     *   val notes = getAllNotesUseCase.execute()
     */
    operator fun invoke(): Flow<List<Note>> {
        return repository.getAllNotes()
    }
}

