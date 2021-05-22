package com.bakhus.noteapp.ui.notes

import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.viewbinding.library.fragment.viewBinding
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bakhus.noteapp.R
import com.bakhus.noteapp.databinding.FragmentNotesBinding
import com.bakhus.noteapp.ui.BaseFragment
import com.bakhus.noteapp.utils.Constants.KEY_LOGGED_IN_EMAIL
import com.bakhus.noteapp.utils.Constants.KEY_PASSWORD
import com.bakhus.noteapp.utils.Constants.NO_EMAIL
import com.bakhus.noteapp.utils.Constants.NO_PASSWORD
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotesFragment() : BaseFragment(R.layout.fragment_notes) {


    private val binding: FragmentNotesBinding by viewBinding()

    @Inject
    lateinit var sharedPref: SharedPreferences


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

    }

    private fun logout() {
        sharedPref.edit().putString(KEY_LOGGED_IN_EMAIL, NO_EMAIL).apply()
        sharedPref.edit().putString(KEY_PASSWORD, NO_PASSWORD).apply()
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.notesFragment, true)
            .build()
        findNavController().navigate(
            NotesFragmentDirections.actionNotesFragmentToAuthFragment(),
            navOptions
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miLogOut -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notes, menu)
    }

}