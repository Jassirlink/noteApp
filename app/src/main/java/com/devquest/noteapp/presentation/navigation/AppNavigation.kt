package com.devquest.noteapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devquest.noteapp.presentation.detail.NoteDetailScreen
import com.devquest.noteapp.presentation.list.NoteListScreen

/**
 * AppNavigation - Sets up the Navigation Graph for the app.
 *
 * CONCEPT: Jetpack Navigation Component
 * ---------------------------------------
 * Navigation Component manages screen transitions (called "navigation").
 * Instead of starting Activities, we navigate between Composable screens.
 *
 * Key concepts:
 * - NavController: tracks the current screen and the back stack
 * - NavHost: the container that renders the current screen
 * - Route: a string identifier for each screen (like a URL)
 * - Arguments: data passed between screens (like URL parameters)
 *
 * CONCEPT: Back Stack
 * --------------------
 * When you navigate to Screen B from Screen A:
 * Back Stack: [A, B]
 * When user presses back, B is popped → [A]
 * navController.popBackStack() does this programmatically.
 *
 * ROUTES defined here:
 * - "note_list"            → NoteListScreen (home screen)
 * - "note_detail/{noteId}" → NoteDetailScreen (add or edit)
 *   - noteId = -1 means "add new note"
 *   - noteId = 123 means "edit note with id=123"
 */
@Composable
fun AppNavigation() {

    // rememberNavController() creates and remembers a NavController.
    // It survives recomposition but is recreated on configuration changes
    // (the back stack is also saved/restored automatically).
    val navController = rememberNavController()

    // NavHost: defines all the screens (destinations) and their routes
    NavHost(
        navController = navController,
        startDestination = "note_list" // The first screen to show
    ) {

        // ---- Screen 1: Note List ----
        composable(route = "note_list") {
            NoteListScreen(
                // When a note is tapped, navigate to detail with that note's ID
                onNoteClick = { noteId ->
                    navController.navigate("note_detail/$noteId")
                },
                // When FAB is tapped, navigate to detail with -1 (= add mode)
                onAddNote = {
                    navController.navigate("note_detail/-1")
                }
            )
        }

        // ---- Screen 2: Note Detail (Add or Edit) ----
        // {noteId} is a route argument — like a path parameter in a URL
        composable(
            route = "note_detail/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    // Tell Navigation the type so it can extract/serialize it
                    type = NavType.IntType
                    defaultValue = -1 // Fallback if no argument is provided
                }
            )
        ) { backStackEntry ->
            // Read the noteId argument from the back stack entry
            val noteId = backStackEntry.arguments?.getInt("noteId") ?: -1

            NoteDetailScreen(
                noteId = noteId,
                onNavigateBack = {
                    // Go back to the previous screen (Note List)
                    navController.popBackStack()
                }
            )
        }
    }
}

