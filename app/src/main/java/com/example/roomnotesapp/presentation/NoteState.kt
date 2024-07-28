package com.example.roomnotesapp.presentation

import android.content.ClipDescription
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.roomnotesapp.data.Note

data class NoteState(
    val note: List<Note> = emptyList(),
    val title: MutableState<String> = mutableStateOf(""),
    val description: MutableState<String> = mutableStateOf("")
)