package com.bakhus.noteapp.ui.notedetail

import androidx.lifecycle.ViewModel
import com.bakhus.noteapp.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    fun observeNoteByID(noteID:String) = repository.observeNoteByID(noteID)

}