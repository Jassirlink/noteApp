package com.devquest.noteapp.presentation.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devquest.noteapp.domain.model.Note
import java.text.SimpleDateFormat
import java.util.*

/**
 * NoteListScreen - The main screen showing all notes.
 *
 * CONCEPT: Composable Functions
 * -----------------------------
 * @Composable functions describe the UI declaratively.
 * Instead of "how to draw," you say "what to show."
 * Compose re-runs the function whenever state changes — this is called "recomposition."
 *
 * CONCEPT: hiltViewModel()
 * -------------------------
 * Gets the ViewModel from Hilt's DI container.
 * Compose Navigation + Hilt handle the ViewModel lifecycle for you.
 *
 * CONCEPT: collectAsState()
 * --------------------------
 * Converts a StateFlow into Compose State.
 * Whenever the StateFlow emits a new value, Compose re-renders this composable.
 *
 * @param onNoteClick Called when user taps a note → navigates to edit screen
 * @param onAddNote   Called when user taps FAB → navigates to add screen
 * @param viewModel   Injected by Hilt automatically
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    onNoteClick: (Int) -> Unit,        // callback: pass note ID to navigate
    onAddNote: () -> Unit,             // callback: navigate to add note screen
    viewModel: NoteListViewModel = hiltViewModel() // Hilt provides this
) {
    // Observe the ViewModel state — UI rebuilds when state changes
    val state by viewModel.state.collectAsState()

    // Show error as a Snackbar if there is one
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    Scaffold(
        // TopAppBar: the bar at the top with the title
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Notes",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        // FAB: Floating Action Button to add a new note
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNote,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        // The content area below the TopAppBar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Loading state
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Empty state: no notes yet
                state.notes.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No notes yet!",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap + to create your first note",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Normal state: show list of notes
                else -> {
                    // LazyColumn = efficient scrollable list (like RecyclerView)
                    // It only renders visible items, not all items at once
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // items() is a LazyColumn extension that iterates the list
                        items(
                            items = state.notes,
                            key = { note -> note.id ?: 0 } // stable key for efficient recomposition
                        ) { note ->
                            NoteCard(
                                note = note,
                                onNoteClick = { note.id?.let { onNoteClick(it) } },
                                onDeleteClick = { viewModel.deleteNote(note) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * NoteCard - A single note item in the list.
 *
 * CONCEPT: Component-based UI
 * ----------------------------
 * We break the UI into small, reusable composable functions.
 * NoteCard is responsible for just ONE note's visual representation.
 * It's "dumb" — it just receives data and callbacks, no business logic.
 */
@Composable
fun NoteCard(
    note: Note,
    onNoteClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onNoteClick() }, // Tap to edit
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // Note content (title + preview)
            Column(
                modifier = Modifier.weight(1f) // Take remaining space, leave room for delete icon
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // "..." if too long
                )

                if (note.content.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatTimestamp(note.timestamp),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            // Delete button
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Note",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Helper function to format the timestamp into a human-readable date.
 * e.g., "Oct 29, 2026 5:10 PM"
 */
private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

