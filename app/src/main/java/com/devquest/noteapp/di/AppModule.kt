package com.devquest.noteapp.di

import com.devquest.noteapp.data.repository.NoteRepositoryImpl
import com.devquest.noteapp.domain.repository.NoteRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * AppModule - Hilt Module that wires interfaces to their implementations.
 *
 * CONCEPT: @Binds
 * ---------------
 * @Binds is used when you want to bind an interface to its implementation.
 * It's more efficient than @Provides because it doesn't need a function body —
 * Hilt generates the binding at compile time.
 *
 * This tells Hilt:
 * "When someone asks for NoteRepository (interface),
 *  give them NoteRepositoryImpl (the concrete class)."
 *
 * This is the KEY to Dependency Inversion:
 * - Domain layer asks for: NoteRepository (interface)
 * - Hilt provides: NoteRepositoryImpl (concrete class from data layer)
 * - The domain layer never knows about the implementation!
 *
 * NOTE: @Binds requires an abstract class (not object), because it generates
 * the implementation at compile time.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        noteRepositoryImpl: NoteRepositoryImpl
    ): NoteRepository
}

