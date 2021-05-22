package com.bakhus.noteapp.ui

import androidx.fragment.app.Fragment
import com.bakhus.noteapp.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment(layoutId: Int) : Fragment(layoutId) {

    fun showSnackbar(text:String){
        Snackbar.make(
            //requireActivity().rootLayout
            requireActivity().findViewById(R.id.content),
            text,
            Snackbar.LENGTH_LONG
        ).show()
    }

}