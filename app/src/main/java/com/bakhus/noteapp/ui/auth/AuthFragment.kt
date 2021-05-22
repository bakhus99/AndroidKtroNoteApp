package com.bakhus.noteapp.ui.auth

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.navigation.fragment.findNavController
import com.bakhus.noteapp.R
import com.bakhus.noteapp.databinding.FragmentAuthBinding
import com.bakhus.noteapp.ui.BaseFragment

class AuthFragment() : BaseFragment(R.layout.fragment_auth) {

    private val binding: FragmentAuthBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val action = AuthFragmentDirections.actionAuthFragmentToNotesFragment()
            findNavController().navigate(action)
        }

    }

}