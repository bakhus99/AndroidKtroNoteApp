package com.bakhus.noteapp.repository

import android.app.Application
import com.bakhus.noteapp.data.local.NoteDao
import com.bakhus.noteapp.data.remote.NoteApi
import com.bakhus.noteapp.data.remote.requests.AccountRequest
import com.bakhus.noteapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val api: NoteApi,
    private val context: Application
) {

    suspend fun register(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val response = api.register(AccountRequest(email, password))
            if (response.isSuccessful) {
                Resource.success(response.body()?.message)
            } else {
                Resource.error(response.message(), null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't connect to the servers.Check your internet connection", null)
        }
    }

}