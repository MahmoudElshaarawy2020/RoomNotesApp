package com.example.roomnotesapp.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomnotesapp.data.Note
import com.example.roomnotesapp.data.NoteDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(private val dao: NoteDao): ViewModel() {

    private val isSortedByDateAdded = MutableStateFlow(true)
    private val notes = isSortedByDateAdded.flatMapLatest {
        sort ->
        if(sort){
            dao.getNotesOrderedByDateAdded()
        }else{
            dao.getNotesOrderedByTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(NoteState())
    val state = combine(_state, isSortedByDateAdded, notes) {state, isSortedByDateAdded, notes ->
        state.copy(
            note = notes
        )
    }

    fun onEvent(event: NotesEvent){
        when(event){
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }
            is NotesEvent.SaveNote -> {
                val note = Note(
                    title = _state.value.title.value,
                    description = _state.value.description.value,
                    dateAdded = System.currentTimeMillis()
                )
                viewModelScope.launch {
                    dao.insertNote(note)
                }

                _state.update {
                    it.copy(
                    title = mutableStateOf(""),
                    description = mutableStateOf("")
                )
                }

            }
            is NotesEvent.SortNotes -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }
        }
    }
}