package com.bakhus.noteapp.ui.notedetail

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bakhus.noteapp.R
import com.bakhus.noteapp.data.local.entites.Note
import com.bakhus.noteapp.databinding.FragmentNoteDetailBinding
import com.bakhus.noteapp.ui.BaseFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

@AndroidEntryPoint
class NoteDetailFragment() : BaseFragment(R.layout.fragment_note_detail) {

    private val args: NoteDetailFragmentArgs by navArgs()
    private val binding: FragmentNoteDetailBinding by viewBinding()
    private val viewModel: NoteDetailViewModel by viewModels()

    private var currentNote: Note? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        binding.fabEditNote.setOnClickListener {
            val action =
                NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(args.id)
            findNavController().navigate(action)
        }
    }


    private fun setMarkdownText(text: String) {
        val markwon = Markwon.create(requireContext())
        val markdown = markwon.toMarkdown(text)
        markwon.setParsedMarkdown(binding.tvNoteContent, markdown)
    }

    private fun subscribeToObservers() {
        viewModel.observeNoteByID(args.id).observe(viewLifecycleOwner, Observer {
            it?.let { note ->
                binding.tvNoteTitle.text = note.title
                setMarkdownText(note.content)
                currentNote = note
            } ?: Snackbar.make(requireView(), "Note not found", Snackbar.LENGTH_SHORT).show()
        })
    }


}