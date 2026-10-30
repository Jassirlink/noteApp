package com.devquest.noteapp.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devquest.noteapp.domain.model.Note
import com.devquest.noteapp.domain.usecase.AddNoteUseCase
import com.devquest.noteapp.domain.usecase.GetNoteByIdUseCase
import com.devquest.noteapp.domain.usecase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * NoteDetailViewModel - ViewModel for Add/Edit Note Screen.
 *
 * CONCEPT: SavedStateHandle
 * --------------------------
 * SavedStateHandle lets the ViewModel read navigation arguments.
 * When we navigate to this screen, we can pass a noteId as an argument.
 * SavedStateHandle reads it here.
 *
 * It also survives process death (when Android kills the app in background).
 * Normal ViewModel state would be lost, but SavedStateHandle preserves it.
 *
 * Hilt injects SavedStateHandle automatically — no extra setup needed!
 */
@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(NoteDetailState())
    val state: StateFlow<NoteDetailState> = _state.asStateFlow()

    init {
        // Check if we received a noteId from navigation arguments.
        // "noteId" must match the route argument name in AppNavigation.kt
        val noteId = savedStateHandle.get<Int>("noteId")

        if (noteId != null && noteId != -1) {
            // Edit mode: load the existing note
            loadNote(noteId)
        }
        // If noteId is null or -1, we're in "Add" mode — nothing to load
    }

    /**
     * Loads an existing note by ID and populates the state.
     */
    private fun loadNote(id: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val note = getNoteByIdUseCase(id)
                if (note != null) {
                    _state.value = _state.value.copy(
                        noteId = note.id,
                        title = note.title,
                        content = note.content,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Failed to load note: ${e.message}"
                )
            }
        }
    }

    /**
     * Called every time the user types in the title field.
     * Updates the state with the new title text.
     */
    fun onTitleChange(newTitle: String) {
        _state.value = _state.value.copy(title = newTitle)
    }

    /**
     * Called every time the user types in the content field.
     */
    fun onContentChange(newContent: String) {
        _state.value = _state.value.copy(content = newContent)
    }

    /**
     * Saves the note — either inserts (add mode) or updates (edit mode).
     * After saving, sets isSaved = true to tell the UI to navigate back.
     *
     * CONCEPT: Error handling in ViewModel
     * -------------------------------------
     * We catch exceptions from the use case (e.g., blank title) and surface
     * them as an error string in state. The UI then shows them in a Snackbar.
     * We never let exceptions crash the UI thread.
     */
    fun saveNote() {
        viewModelScope.launch {
            try {
                val currentState = _state.value

                if (currentState.noteId == null) {
                    // ADD MODE: create a brand-new note
                    addNoteUseCase(
                        Note(
                            title = currentState.title,
                            content = currentState.content
                        )
                    )
                } else {
                    // EDIT MODE: update the existing note (keep the same id)
                    updateNoteUseCase(
                        Note(
                            id = currentState.noteId,
                            title = currentState.title,
                            content = currentState.content
                        )
                    )
                }

                // Signal success — the UI will observe this and navigate back
                _state.value = _state.value.copy(isSaved = true)

            } catch (e: IllegalArgumentException) {
                // Business rule violation (e.g., empty title) — show to user
                _state.value = _state.value.copy(error = e.message)
            } catch (e: Exception) {
                // Unexpected error
                _state.value = _state.value.copy(error = "Failed to save note: ${e.message}")
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

