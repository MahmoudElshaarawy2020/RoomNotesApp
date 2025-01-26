package com.example.roomnotesapp.presentation

import com.example.roomnotesapp.data.Note

// Represents different events that can occur in the Notes screen
sealed interface NotesEvent {
    object SortNotes: NotesEvent

    data class DeleteNote(val note: Note): NotesEvent

    data class SaveNote(
        val title:String,
        val description: String
    ):NotesEvent
    data class UpdateTitle(val title: String) : NotesEvent
    data class UpdateDescription(val description: String) : NotesEvent
}
