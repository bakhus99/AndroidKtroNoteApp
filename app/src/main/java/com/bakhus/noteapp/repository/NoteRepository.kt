package com.bakhus.noteapp.repository

import android.app.Application
import com.bakhus.noteapp.data.local.NoteDao
import com.bakhus.noteapp.data.local.entites.Note
import com.bakhus.noteapp.data.remote.NoteApi
import com.bakhus.noteapp.data.remote.requests.AccountRequest
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
                    //TODO: insert notes in db
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