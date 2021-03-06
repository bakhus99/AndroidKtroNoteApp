package com.bakhus.noteapp.ui.auth

import android.content.SharedPreferences
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bakhus.noteapp.R
import com.bakhus.noteapp.data.remote.BasicAuthInterceptor
import com.bakhus.noteapp.databinding.FragmentAuthBinding
import com.bakhus.noteapp.utils.Constants.KEY_LOGGED_IN_EMAIL
import com.bakhus.noteapp.utils.Constants.KEY_PASSWORD
import com.bakhus.noteapp.utils.Constants.NO_EMAIL
import com.bakhus.noteapp.utils.Constants.NO_PASSWORD
import com.bakhus.noteapp.utils.Status
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val viewModel: AuthViewModel by viewModels()
    private val binding: FragmentAuthBinding by viewBinding()

    @Inject
    lateinit var sharedPref: SharedPreferences

    @Inject
    lateinit var basicAuthInterceptor: BasicAuthInterceptor

    private var curEmail: String? = null
    private var curPassword: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isLoggedIn()) {
            authenticateApi(curEmail ?: "", curPassword ?: "")
            redirectLogin()
        }

        requireActivity().requestedOrientation = SCREEN_ORIENTATION_PORTRAIT

        subscribeToObservers()

        binding.btnRegister.setOnClickListener {
            val email = binding.etRegisterEmail.text.toString()
            val password = binding.etRegisterPassword.text.toString()
            val confirmedPassword = binding.etRegisterPasswordConfirm.text.toString()
            viewModel.register(email, password, confirmedPassword)

        }
        binding.btnLogin.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()
            curEmail = email
            curPassword = password
            viewModel.login(email, password)
        }

    }

    private fun isLoggedIn(): Boolean {
        curEmail = sharedPref.getString(KEY_LOGGED_IN_EMAIL, NO_EMAIL) ?: NO_EMAIL
        curPassword = sharedPref.getString(KEY_PASSWORD, NO_PASSWORD) ?: NO_PASSWORD
        return curEmail != NO_EMAIL && curPassword != NO_PASSWORD
    }

    private fun authenticateApi(email: String, password: String) {
        basicAuthInterceptor.email = email
        basicAuthInterceptor.password = password
    }


    private fun redirectLogin() {
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.authFragment, true)
            .build()

        findNavController().navigate(
            AuthFragmentDirections.actionAuthFragmentToNotesFragment(),
            navOptions
        )
    }


    private fun subscribeToObservers() {
        viewModel.loginStatus.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when (result.status) {
                    Status.SUCCESS -> {
                        binding.loginProgressBar.visibility = View.GONE
                        Snackbar.make(
                            requireView(),
                            result.data ?: requireContext().getString(R.string.succes_log_in), Snackbar.LENGTH_SHORT
                        ).show()
                        sharedPref.edit().putString(KEY_LOGGED_IN_EMAIL, curEmail).apply()
                        sharedPref.edit().putString(KEY_PASSWORD, curPassword).apply()
                        authenticateApi(curEmail ?: "", curPassword ?: "")
                        redirectLogin()
                    }
                    Status.ERROR -> {
                        binding.loginProgressBar.visibility = View.GONE
                        Snackbar.make(
                            requireView(),
                            result.message ?: requireContext().getString(R.string.unknown_error), Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    Status.LOADING -> {
                        binding.loginProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        })

        viewModel.registerStatus.observe(viewLifecycleOwner, Observer { result ->
            result?.let {
                when (result.status) {
                    Status.SUCCESS -> {
                        binding.registerProgressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            result.data ?: requireContext().getString(R.string.succes_register_acc),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    Status.ERROR -> {
                        binding.registerProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), result.message ?: requireContext().getString(R.string.unknown_error), Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        binding.registerProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

}