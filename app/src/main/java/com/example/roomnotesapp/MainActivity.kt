package com.example.roomnotesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.roomnotesapp.data.NotesDatabase
import com.example.roomnotesapp.presentation.AddNoteScreen
import com.example.roomnotesapp.presentation.NoteState
import com.example.roomnotesapp.presentation.NotesScreen
import com.example.roomnotesapp.presentation.NotesViewModel
import com.example.roomnotesapp.ui.theme.RoomNotesAppTheme

class MainActivity : ComponentActivity() {

    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            NotesDatabase::class.java,
            "notes.db"
        ).build()
    }

    private val viewModel by viewModels<NotesViewModel>(
        factoryProducer = {object :ViewModelProvider.Factory{
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NotesViewModel(database.dao) as T
            }
        }}
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RoomNotesAppTheme {
                val state by viewModel.state.collectAsState(NoteState())
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "NotesScreen"){
                    composable("NotesScreen"){
                        NotesScreen(
                            state = state,
                            navController = navController,
                            onEvent = viewModel::onEvent,
                            viewModel
                        )
                    }

                    composable("AddNoteScreen"){
                        AddNoteScreen(
                            state = state,
                            navController = navController,
                            onEvent = viewModel::onEvent,
                            viewModel
                        )
                    }
                }
            }
        }
    }
}

