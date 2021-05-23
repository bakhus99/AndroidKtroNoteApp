package com.bakhus.noteapp.ui.notedetail

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bakhus.noteapp.R
import com.bakhus.noteapp.databinding.FragmentNoteDetailBinding
import com.bakhus.noteapp.ui.BaseFragment

class NoteDetailFragment() : BaseFragment(R.layout.fragment_note_detail) {

    private val args: NoteDetailFragmentArgs by navArgs()
    private val binding: FragmentNoteDetailBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabEditNote.setOnClickListener {
            val action =
                NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(args.id)
            findNavController().navigate(action)
        }
    }


}