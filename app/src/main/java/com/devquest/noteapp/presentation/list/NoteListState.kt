package com.devquest.noteapp.presentation.list

import com.devquest.noteapp.domain.model.Note

/**
 * NoteListState - The UI State for the Note List Screen.
 *
 * CONCEPT: UI State
 * -----------------
 * In MVVM + Compose, we model all the information the UI needs as a single state object.
 * The ViewModel produces this state, the Composable just renders it.
 *
 * WHY a single state class?
 * - Predictable: the UI is ALWAYS a function of this state
 * - Testable: just check what state the ViewModel produces
 * - No scattered boolean flags across the ViewModel
 *
 * This is called "Unidirectional Data Flow" (UDF):
 * UI events → ViewModel → State → UI renders
 */
data class NoteListState(
    val notes: List<Note> = emptyList(),   // The list of all notes
    val isLoading: Boolean = false,         // Show a loading spinner?
    val error: String? = null               // Show an error message?
)

