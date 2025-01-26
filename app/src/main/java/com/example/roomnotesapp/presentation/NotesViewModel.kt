package com.example.roomnotesapp.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomnotesapp.data.Note
import com.example.roomnotesapp.data.NoteDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotesViewModel(private val dao: NoteDao) : ViewModel() {

    private val isSortedByDateAdded = MutableStateFlow(true)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val notes = isSortedByDateAdded.flatMapLatest { sort ->
        if (sort) {
            dao.getNotesOrderedByDateAdded()
        } else {
            dao.getNotesOrderedByTitle()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(NoteState())
    val state = combine(_state, isSortedByDateAdded, notes) { state, isSortedByDateAdded, notes ->
        state.copy(
            note = notes
        )
    }


    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    dao.deleteNote(event.note)
                }
            }

            is NotesEvent.SaveNote -> {
                viewModelScope.launch {
                    dao.insertNote(
                        note = Note(
                            title = event.title,
                            description = event.description,
                            dateAdded = System.currentTimeMillis()
                        )
                    )
                }
            }

            is NotesEvent.SortNotes -> {
                isSortedByDateAdded.value = !isSortedByDateAdded.value
            }

            is NotesEvent.UpdateTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            is NotesEvent.UpdateDescription -> {
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }
            }
        }
    }


    fun removeItem(note: Note) {
        viewModelScope.launch {
            dao.deleteNote(note)
        }
        _state.update {
            it.copy(
                title = "",  // Use plain strings
                description = ""  // Use plain strings
            )
            val mutableList = it.note.toMutableStateList()
            mutableList.remove(note)
            it.copy(note = mutableList)
        }
    }

}