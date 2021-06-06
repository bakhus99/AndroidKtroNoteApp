package com.bakhus.noteapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bakhus.noteapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_NoteApp)
        setContentView(R.layout.activity_main)
    }
}