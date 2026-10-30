package com.devquest.noteapp.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * NoteDetailScreen - Screen for adding a new note OR editing an existing one.
 *
 * CONCEPT: Single Screen for Add AND Edit
 * ----------------------------------------
 * We reuse this one screen for both use cases by checking the noteId:
 * - noteId = -1 (or null in ViewModel) → Add mode
 * - noteId = valid ID → Edit mode
 *
 * The ViewModel handles the difference — the UI just binds to state.
 *
 * CONCEPT: Two-way data binding in Compose
 * -----------------------------------------
 * Unlike XML + DataBinding, Compose does "one-way" binding:
 * 1. The TextField displays state.title (read from ViewModel)
 * 2. When user types, onValueChange fires → calls viewModel.onTitleChange()
 * 3. ViewModel updates state.title
 * 4. Compose sees state changed → recomposition → TextField shows new value
 *
 * @param noteId     The ID of the note to edit, or -1 for a new note
 * @param onNavigateBack Called when save is complete or user presses back
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Int,
    onNavigateBack: () -> Unit,
    viewModel: NoteDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Show error Snackbar when an error occurs
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }

    // Navigate back automatically when the note is saved
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    // Show different title depending on add vs edit mode
                    Text(text = if (noteId == -1) "Add Note" else "Edit Note")
                },
                // Back arrow button
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        // Save button as FAB
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.saveNote() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Note"
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        // Show loading spinner while fetching an existing note
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // The form: title + content text fields
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // ---- Title Field ----
                // CONCEPT: OutlinedTextField
                // A Material3 text field. value + onValueChange = two-way binding pattern.
                OutlinedTextField(
                    value = state.title,
                    onValueChange = { viewModel.onTitleChange(it) },
                    label = { Text("Title") },
                    placeholder = { Text("Enter note title...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true, // Title is one line only
                    textStyle = MaterialTheme.typography.titleLarge,
                    isError = state.error != null && state.title.isBlank()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // ---- Content Field ----
                // weight(1f) makes this take all remaining vertical space
                OutlinedTextField(
                    value = state.content,
                    onValueChange = { viewModel.onContentChange(it) },
                    label = { Text("Content") },
                    placeholder = { Text("Write your note here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f), // Expand to fill remaining height
                    textStyle = MaterialTheme.typography.bodyLarge
                )

                // Padding at the bottom so the FAB doesn't overlap text
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

