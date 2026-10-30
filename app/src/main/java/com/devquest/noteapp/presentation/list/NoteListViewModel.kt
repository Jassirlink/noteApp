package com.devquest.noteapp.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devquest.noteapp.domain.model.Note
import com.devquest.noteapp.domain.usecase.DeleteNoteUseCase
import com.devquest.noteapp.domain.usecase.GetAllNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * NoteListViewModel - ViewModel for the Note List Screen.
 *
 * CONCEPT: ViewModel
 * ------------------
 * A ViewModel survives configuration changes (e.g., screen rotation).
 * It holds and manages UI-related data.
 * The UI (Composable) observes the ViewModel's state and re-renders when it changes.
 *
 * CONCEPT: @HiltViewModel
 * ------------------------
 * Tells Hilt how to create this ViewModel.
 * Combined with @Inject constructor, Hilt injects the use cases automatically.
 * In the Composable, we get this ViewModel using: hiltViewModel()
 *
 * CONCEPT: StateFlow
 * ------------------
 * StateFlow is like LiveData but for Kotlin Coroutines.
 * - MutableStateFlow: the ViewModel writes to this
 * - StateFlow (exposed via asStateFlow()): the UI reads from this (read-only)
 * This separation prevents the UI from accidentally changing the state.
 *
 * CONCEPT: viewModelScope
 * -----------------------
 * A CoroutineScope tied to the ViewModel's lifecycle.
 * When the ViewModel is destroyed, all coroutines in this scope are cancelled.
 * This prevents memory leaks!
 */
@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    // _state is private and mutable — only the ViewModel can change it
    private val _state = MutableStateFlow(NoteListState())

    // state is public and read-only — the UI observes this
    val state: StateFlow<NoteListState> = _state.asStateFlow()

    init {
        // When the ViewModel is created, immediately start loading notes
        loadNotes()
    }

    private fun loadNotes() {
        // getAllNotesUseCase() returns a Flow<List<Note>>
        getAllNotesUseCase()
            .onEach { notes ->
                // Each time the database changes, update the state with new notes
                _state.value = _state.value.copy(
                    notes = notes,
                    isLoading = false
                )
            }
            .launchIn(viewModelScope) // Collect the Flow in the ViewModel's scope
    }

    /**
     * Called when the user taps the delete button on a note.
     * Runs in a coroutine because deleteNoteUseCase is a suspend function.
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                deleteNoteUseCase(note)
                // No need to update state manually — the Flow from getAllNotes
                // will automatically emit the updated list!
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Failed to delete note: ${e.message}"
                )
            }
        }
    }

    /**
     * Called after the UI has shown the error message, to clear it.
     */
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}

