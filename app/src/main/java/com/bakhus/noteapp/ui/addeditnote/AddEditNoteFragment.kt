package com.bakhus.noteapp.ui.addeditnote

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bakhus.noteapp.R
import com.bakhus.noteapp.data.local.entites.Note
import com.bakhus.noteapp.databinding.FragmentAddEditNoteBinding
import com.bakhus.noteapp.ui.BaseFragment
import com.bakhus.noteapp.ui.dialog.ColorPickerDialogFragment
import com.bakhus.noteapp.utils.Constants.DEFAULT_NOTE_COLOR
import com.bakhus.noteapp.utils.Constants.FRAGMENT_TAG
import com.bakhus.noteapp.utils.Constants.KEY_LOGGED_IN_EMAIL
import com.bakhus.noteapp.utils.Constants.NO_EMAIL
import com.bakhus.noteapp.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AddEditNoteFragment() : BaseFragment(R.layout.fragment_add_edit_note) {

    private val viewModel: AddEditNoteViewModel by viewModels()
    private val binding: FragmentAddEditNoteBinding by viewBinding()
    private val args: AddEditNoteFragmentArgs by navArgs()

    private var currentNote: Note? = null
    private var currentNoteColor = DEFAULT_NOTE_COLOR

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val TAG = "AddEditNoteFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.id.isNotEmpty()) {
            viewModel.getNoteById(args.id)
            subscribeToObservers()
        }

        if (savedInstanceState != null) {
            val colorPickerDialog = parentFragmentManager.findFragmentByTag(FRAGMENT_TAG)
                    as ColorPickerDialogFragment?
            colorPickerDialog?.setPositiveListener {
                changeViewNoteColor(it)
            }
        }

        binding.viewNoteColor.setOnClickListener {
            ColorPickerDialogFragment().apply {
                setPositiveListener {
                    changeViewNoteColor(it)
                }
            }.show(parentFragmentManager, FRAGMENT_TAG)
        }
        binding.fabSaveNote.setOnClickListener {
            saveNote()
            val action = AddEditNoteFragmentDirections.actionAddEditNoteFragmentToNotesFragment()
            findNavController().navigate(action)

        }
    }

    private fun changeViewNoteColor(colorString: String) {
        val drawable = ResourcesCompat.getDrawable(resources, R.drawable.circle_shape, null)
        drawable?.let {
            val wrapperDrawable = DrawableCompat.wrap(it)
            val color = Color.parseColor("#$colorString")
            DrawableCompat.setTint(wrapperDrawable, color)
            binding.viewNoteColor.background = wrapperDrawable
            currentNoteColor = colorString
        }
    }

    private fun subscribeToObservers() {
        viewModel.note.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        val note = result.data!!
                        currentNote = note
                        binding.etNoteTitle.setText(note.title)
                        binding.etNoteContent.setText(note.content)
                        changeViewNoteColor(note.color)
                    }
                    Status.ERROR -> {
                        Toast.makeText(
                            requireContext(),
                            result.message ?: "Note not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Status.LOADING -> {
                        /* NO-OP */
                    }
                }
            }
        })
    }

//    override fun onPause() {
//        super.onPause()
//        saveNote()
//    }

    private fun saveNote() {
        val authEmail = sharedPreferences.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL

        val title = binding.etNoteTitle.text.toString()
        val content = binding.etNoteContent.text.toString()

        if (title.isEmpty() || content.isEmpty()) {
            return
        }
        val date = System.currentTimeMillis()
        val color = currentNoteColor
        val id = currentNote?.id ?: UUID.randomUUID().toString()
        val owners = currentNote?.owners ?: listOf(authEmail)

        val note = Note(title, content, date, owners, color, id = id)

        viewModel.insertNote(note)
    }
}

