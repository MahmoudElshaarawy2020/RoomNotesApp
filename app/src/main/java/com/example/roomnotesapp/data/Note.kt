package com.example.roomnotesapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note") // Ensure the table name matches the DAO query
data class Note(
    val title: String,
    val description: String,
    val dateAdded: Long, // Ensure the data type is correct

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0 // Ensure primary key is auto-generated
)
