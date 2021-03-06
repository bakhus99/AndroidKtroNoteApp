package com.bakhus.noteapp.data.remote

import com.bakhus.noteapp.data.local.entites.Note
import com.bakhus.noteapp.data.remote.requests.AccountRequest
import com.bakhus.noteapp.data.remote.requests.AddOwnerRequest
import com.bakhus.noteapp.data.remote.requests.DeleteNoteRequest
import com.bakhus.noteapp.data.remote.responses.SimpleResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NoteApi {

    @POST("/register")
    suspend fun register(@Body registerRequest: AccountRequest): Response<SimpleResponse>
    @POST("/login")
    suspend fun login(@Body loginRequest: AccountRequest): Response<SimpleResponse>
    @POST("/addNote")
    suspend fun addNote(@Body note: Note): Response<ResponseBody>
    @POST("/deleteNote")
    suspend fun deleteNoteRequest(@Body deleteNoteRequest: DeleteNoteRequest): Response<ResponseBody>
    @GET("/getNotes")
    suspend fun getNotes():Response<List<Note>>

    @POST("/addOwnerToNote")
    suspend fun addOwnerToNote(@Body addOwnerRequest: AddOwnerRequest): Response<SimpleResponse>

}