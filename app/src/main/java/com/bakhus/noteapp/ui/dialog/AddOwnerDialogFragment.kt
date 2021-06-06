package com.bakhus.noteapp.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.bakhus.noteapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_note_detail.*

class AddOwnerDialogFragment : DialogFragment() {

    private var positiveListener: ((String) -> Unit)? = null

    fun setPositiveListener(listener: (String) -> Unit) {
        positiveListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val addOwnerAddedText = LayoutInflater.from(requireContext()).inflate(
            R.layout.edit_text_email,
            clNoteContainer,
            false
        ) as TextInputLayout
        return MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.ic_add_person)
            .setTitle(requireContext().getString(R.string.add_owner_note))
            .setMessage(
                requireContext().getString(R.string.add_owner_note_message)
            )
            .setView(addOwnerAddedText)
            .setPositiveButton(requireContext().getString(R.string.add_text)) { _, _ ->
                val email =
                    addOwnerAddedText.findViewById<EditText>(R.id.etAddOwnerEmail).text.toString()
                positiveListener?.let { yes ->
                    yes(email)
                }
            }
            .setNegativeButton(requireContext().getString(R.string.cancel_text)) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
    }


}