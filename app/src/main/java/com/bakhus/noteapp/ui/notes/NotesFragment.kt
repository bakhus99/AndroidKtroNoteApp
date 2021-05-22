package com.bakhus.noteapp.ui.notes

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.viewModels
import com.bakhus.noteapp.R
import com.bakhus.noteapp.databinding.FragmentNotesBinding
import com.bakhus.noteapp.ui.BaseFragment

class NotesFragment() : BaseFragment(R.layout.fragment_notes) {


    private val binding: FragmentNotesBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}