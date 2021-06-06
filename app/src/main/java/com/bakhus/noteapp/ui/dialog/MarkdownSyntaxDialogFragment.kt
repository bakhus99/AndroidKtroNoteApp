package com.bakhus.noteapp.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bakhus.noteapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MarkdownSyntaxDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.ic_help)
            .setTitle(requireContext().getString(R.string.markdown_syntax))
            .setMessage("# - for header\n" +
                    " **text** - for bold text\n" +
                    " *text* - italic text\n" +
                    " - 1 -2 - for list\n" +
                    " [ ] - checklist\n" +
                    " [x] - checked")
            .setPositiveButton(requireContext().getString(R.string.got_it)){ dialogInterface,_, ->
                dialogInterface.cancel()
            }
            .create()
    }
}