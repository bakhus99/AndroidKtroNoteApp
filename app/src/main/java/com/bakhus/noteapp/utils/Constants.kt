package com.bakhus.noteapp.utils

object Constants {

    val IGNORE_AUTH_URLS = listOf("/login", "/register")
    const val DATABASE_NAME = "NotesDb"
    const val BASE_URL = "https://188.225.73.173:8089"
    const val ENCRYPTED_SHARED_PREF_NAME = "enc_shared_pref"
    const val KEY_LOGGED_IN_EMAIL = "KEY_LOGGED_IN_EMAIL"
    const val KEY_PASSWORD = "KEY_PASSWORD"
    const val NO_EMAIL = "NO_EMAIL"
    const val NO_PASSWORD = "NO_PASSWORD"
    const val DEFAULT_NOTE_COLOR = "FFA500"
    const val FRAGMENT_TAG = "ADD_EDIT_NOTE_FRAGMENT"
    const val ADD_OWNER_DIALOG_TAG = "ADD_OWNER_DIALOG_TAG"
    const val ADD_OWNER_MARKDOWN_TAG = "ADD_OWNER_MARKDOWN_TAG"
}