package com.devquest.noteapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * NoteApp - The Application class for our Note App.
 *
 * CONCEPT: @HiltAndroidApp
 * -------------------------
 * This annotation tells Dagger Hilt to generate the Hilt component hierarchy.
 * Without this, Hilt won't work at all.
 *
 * Think of this as the "root" of the Dependency Injection tree.
 * Hilt uses this to create a top-level component that lives as long as the app lives.
 *
 * You MUST:
 * 1. Annotate your Application class with @HiltAndroidApp
 * 2. Register it in AndroidManifest.xml as android:name=".NoteApp"
 */
@HiltAndroidApp
class NoteApp : Application()

