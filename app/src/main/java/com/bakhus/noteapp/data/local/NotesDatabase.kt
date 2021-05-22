package com.bakhus.noteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bakhus.noteapp.data.local.entites.Note


@Database(
    entities = [Note::class],
    version = 1
)
abstract class NotesDatabase : RoomDatabase() {

    @TypeConverters(Converters::class)
    abstract fun noteDao(): NoteDao

}