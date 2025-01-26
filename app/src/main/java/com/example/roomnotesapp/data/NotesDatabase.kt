package com.example.roomnotesapp.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [Note::class],
    version = 2 // Increment the version number here
)
abstract class NotesDatabase : RoomDatabase() {
    abstract val dao: NoteDao
}
