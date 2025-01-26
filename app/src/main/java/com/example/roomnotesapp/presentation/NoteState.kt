package com.example.roomnotesapp.presentation

import com.example.roomnotesapp.data.Note

data class NoteState(
    val note: List<Note> = emptyList(),
    var title: String = "", // Use String directly
    var description: String = "" // Use String directly
)

