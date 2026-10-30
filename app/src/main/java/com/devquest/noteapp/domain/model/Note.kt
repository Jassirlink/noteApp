package com.devquest.noteapp.domain.model

/**
 * Note - The Domain Model (Pure Kotlin Data Class)
 *
 * CONCEPT: Domain Layer
 * ----------------------
 * The domain layer is the CORE of Clean Architecture.
 * It contains business logic and data models that have NO dependency on Android.
 *
 * This Note class:
 * - Has NO Room annotations (@Entity, @PrimaryKey, etc.)
 * - Has NO Android imports
 * - Is a pure Kotlin data class
 *
 * WHY SEPARATE FROM NoteEntity?
 * The database (NoteEntity) is an implementation detail.
 * If we switch from Room to another database, we only change the data layer —
 * this domain model stays the same.
 *
 * @param id       Unique identifier. null for a new unsaved note (Room auto-generates the id).
 * @param title    The title of the note.
 * @param content  The body/content of the note.
 * @param timestamp When the note was created/modified (milliseconds since epoch).
 */
data class Note(
    val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)

