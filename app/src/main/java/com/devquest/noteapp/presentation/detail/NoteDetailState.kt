package com.devquest.noteapp.presentation.detail

/**
 * NoteDetailState - The UI State for Add/Edit Note Screen.
 *
 * This screen handles BOTH adding a new note and editing an existing one.
 * - If noteId is null → we're adding a new note
 * - If noteId is not null → we're editing an existing note
 *
 * The title and content fields are bound to the text fields in the UI.
 * When the user types, we update this state.
 */
data class NoteDetailState(
    val noteId: Int? = null,          // null = new note, non-null = edit mode
    val title: String = "",           // Current text in the title field
    val content: String = "",         // Current text in the content field
    val isLoading: Boolean = false,   // True while loading existing note data
    val isSaved: Boolean = false,     // True when save is successful → triggers navigation back
    val error: String? = null         // Error message to show the user
)

