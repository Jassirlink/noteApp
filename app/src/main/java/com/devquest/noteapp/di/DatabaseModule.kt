package com.devquest.noteapp.di

import android.content.Context
import androidx.room.Room
import com.devquest.noteapp.data.local.NoteDao
import com.devquest.noteapp.data.local.NoteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * DatabaseModule - Hilt Module for Database dependencies.
 *
 * CONCEPT: Hilt Module
 * --------------------
 * A @Module is a class that tells Hilt HOW to create certain objects.
 * It's used when you can't add @Inject to a constructor (e.g., Room database,
 * Retrofit, 3rd-party libraries).
 *
 * @InstallIn(SingletonComponent::class) means:
 * These dependencies live as long as the Application (singletons).
 * A SingletonComponent is created once and destroyed when the app is killed.
 *
 * CONCEPT: @Provides
 * ------------------
 * Each @Provides function tells Hilt:
 * "When someone asks for [return type], run this function to create it."
 *
 * CONCEPT: @Singleton
 * -------------------
 * Ensures only ONE instance is created.
 * Without @Singleton, Hilt would create a new instance every time it's needed.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the NoteDatabase singleton.
     *
     * @ApplicationContext: Hilt provides the Application Context automatically.
     * Room.databaseBuilder creates the SQLite database file on the device.
     */
    @Provides
    @Singleton
    fun provideNoteDatabase(
        @ApplicationContext context: Context
    ): NoteDatabase {
        return Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    /**
     * Provides the NoteDao.
     * Hilt calls provideNoteDatabase() to get the database, then calls noteDao() on it.
     */
    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
    }
}

