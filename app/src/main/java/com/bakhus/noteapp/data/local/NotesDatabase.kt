package com.bakhus.noteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bakhus.noteapp.data.local.entites.LocallyDeletedNoteID
import com.bakhus.noteapp.data.local.entites.Note


@Database(
    entities = [Note::class, LocallyDeletedNoteID::class],
    version = 1)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}