package com.bakhus.noteapp.repository

import android.app.Application
import com.bakhus.noteapp.data.local.NoteDao
import com.bakhus.noteapp.data.local.entites.LocallyDeletedNoteID
import com.bakhus.noteapp.data.local.entites.Note
import com.bakhus.noteapp.data.remote.NoteApi
import com.bakhus.noteapp.data.remote.requests.AccountRequest
import com.bakhus.noteapp.data.remote.requests.DeleteNoteRequest
import com.bakhus.noteapp.utils.Resource
import com.bakhus.noteapp.utils.checkForInternetConnection
import com.bakhus.noteapp.utils.networkBoundResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val api: NoteApi,
    private val context: Application
) {

    suspend fun insertNote(note: Note) {
        val response = try {
            api.addNote(note)
        } catch (e: Exception) {
            null
        }
        if (response != null && response.isSuccessful) {
            noteDao.insertNote(note.apply { isSync = true })
        } else {
            noteDao.insertNote(note)
        }
    }

    suspend fun insertNotes(notes: List<Note>) {
        notes.forEach { insertNote(it) }
    }

    suspend fun deleteNote(noteID: String) {
        val response = try {
            api.deleteNoteRequest(DeleteNoteRequest(noteID))
        } catch (e: Exception) {
            null
        }
        noteDao.deleteNoteById(noteID)
        if (response == null || !response.isSuccessful) {
            noteDao.insertLocallyDeletedNoteID(LocallyDeletedNoteID(noteID))
        } else {
            deleteLocallyNoteID(noteID)
        }
    }

    suspend fun deleteLocallyNoteID(deletedNoteID: String) {
        noteDao.deleteLocallyDeletedNoteID(deletedNoteID)
    }

    suspend fun getNoteById(noteID: String) = noteDao.getNoteById(noteID)

    fun getAllNotes(): Flow<Resource<List<Note>>> {
        return networkBoundResource(
            query = {
                noteDao.getAllNotes()
            },
            fetch = {
                api.getNotes()
            },
            saveFetchResult = { response ->
                response.body()?.let {
                    insertNotes(it)
                }

            },
            shouldFetch = {
                checkForInternetConnection(context)
            }
        )
    }

    suspend fun login(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.login(AccountRequest(email, password))
            if (response.isSuccessful && response.body()!!.successful) {
                Resource.success(response.body()?.message)
            } else {
                Resource.error(response.body()?.message ?: response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't connect to the servers.Check your internet connection", null)
        }
    }

    suspend fun register(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.register(AccountRequest(email, password))
            if (response.isSuccessful && response.body()!!.successful) {
                Resource.success(response.body()?.message)
            } else {
                Resource.error(response.body()?.message?: response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't connect to the servers.Check your internet connection", null)
        }
    }

}