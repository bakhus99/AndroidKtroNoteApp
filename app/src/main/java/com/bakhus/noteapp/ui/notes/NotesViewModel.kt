package com.bakhus.noteapp.ui.notes

import androidx.lifecycle.*
import com.bakhus.noteapp.data.local.entites.Note
import com.bakhus.noteapp.repository.NoteRepository
import com.bakhus.noteapp.utils.Event
import com.bakhus.noteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {


    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _allNotes = _forceUpdate.switchMap {
        repository.getAllNotes().asLiveData(viewModelScope.coroutineContext)
    }.switchMap {
        MutableLiveData(Event(it))
    }

    val allNotes: LiveData<Event<Resource<List<Note>>>> = _allNotes

    fun syncAllNotes() = _forceUpdate.postValue(true)

    fun deleteNote(noteID: String) = viewModelScope.launch {
        repository.deleteNote(noteID)
    }

    fun deleteLocallyDeletedNoteID(deletedNoteID: String) = viewModelScope.launch {
        repository.deleteLocallyNoteID(deletedNoteID)
    }

    fun insertNote(note: Note) = viewModelScope.launch {
        repository.insertNote(note)
    }

}