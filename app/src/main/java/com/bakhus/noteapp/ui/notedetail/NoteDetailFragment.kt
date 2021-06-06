package com.bakhus.noteapp.ui.notedetail

import android.os.Bundle
import android.view.*
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bakhus.noteapp.R
import com.bakhus.noteapp.data.local.entites.Note
import com.bakhus.noteapp.databinding.FragmentNoteDetailBinding
import com.bakhus.noteapp.ui.BaseFragment
import com.bakhus.noteapp.ui.dialog.AddOwnerDialogFragment
import com.bakhus.noteapp.ui.dialog.MarkdownSyntaxDialogFragment
import com.bakhus.noteapp.utils.Constants.ADD_OWNER_DIALOG_TAG
import com.bakhus.noteapp.utils.Constants.ADD_OWNER_MARKDOWN_TAG
import com.bakhus.noteapp.utils.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon

@AndroidEntryPoint
class NoteDetailFragment() : BaseFragment(R.layout.fragment_note_detail) {

    private val args: NoteDetailFragmentArgs by navArgs()
    private val binding: FragmentNoteDetailBinding by viewBinding()
    private val viewModel: NoteDetailViewModel by viewModels()

    private var currentNote: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        binding.fabEditNote.setOnClickListener {
            val action =
                NoteDetailFragmentDirections.actionNoteDetailFragmentToAddEditNoteFragment(args.id)
            findNavController().navigate(action)
        }

        if (savedInstanceState != null) {
            val addOwnerDialog = parentFragmentManager.findFragmentByTag(ADD_OWNER_DIALOG_TAG)
                    as AddOwnerDialogFragment?
            addOwnerDialog?.setPositiveListener {
                addOwnerToCurrentNote(it)
            }
        }
    }

    private fun showAddOwnerDialog() {
        AddOwnerDialogFragment().apply {
            setPositiveListener {
                addOwnerToCurrentNote(it)
            }
        }.show(parentFragmentManager, ADD_OWNER_DIALOG_TAG)
    }

    private fun showMarkdownSyntax() {
        MarkdownSyntaxDialogFragment().show(parentFragmentManager, ADD_OWNER_MARKDOWN_TAG)
    }

    private fun addOwnerToCurrentNote(email: String) {
        currentNote?.let { note ->
            viewModel.addOwnerToNote(email, noteID = note.id)
        }
    }


    private fun setMarkdownText(text: String) {
        val markwon = Markwon.create(requireContext())
        val markdown = markwon.toMarkdown(text)
        markwon.setParsedMarkdown(binding.tvNoteContent, markdown)
    }

    private fun subscribeToObservers() {
        viewModel.addOwnerStatus.observe(viewLifecycleOwner, Observer { event ->
            event?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        binding.addOwnerProgressBar.visibility = View.GONE
                        Snackbar.make(
                            requireView(),
                            result.data ?: requireContext().getString(R.string.succes_add_owner_to_note),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    Status.ERROR -> {
                        binding.addOwnerProgressBar.visibility = View.GONE
                        Snackbar.make(
                            requireView(),
                            result.message ?: requireContext().getString(R.string.unknown_error),
                            Snackbar.LENGTH_SHORT
                        ).show()

                    }
                    Status.LOADING -> {
                        binding.addOwnerProgressBar.visibility = View.VISIBLE
                    }
                }

            }

        })
        viewModel.observeNoteByID(args.id).observe(viewLifecycleOwner, Observer {
            it?.let { note ->
                binding.tvNoteTitle.text = note.title
                setMarkdownText(note.content)
                currentNote = note
            } ?: Snackbar.make(requireView(), requireContext().getString(R.string.note_not_found), Snackbar.LENGTH_SHORT).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.note_deatil_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miAddOwner -> {
                showAddOwnerDialog()
            }
            R.id.miMarkdownSyntax -> {
                showMarkdownSyntax()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}