package com.bakhus.noteapp.ui.auth

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bakhus.noteapp.R
import com.bakhus.noteapp.repository.NoteRepository
import com.bakhus.noteapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val context:Application
) : ViewModel() {

    private val _registerStatus = MutableLiveData<Resource<String>>()
    val registerStatus: LiveData<Resource<String>> = _registerStatus

    private val _loginStatus = MutableLiveData<Resource<String>>()
    val loginStatus: LiveData<Resource<String>> = _loginStatus


    fun login(email: String, password: String) {
        _loginStatus.value = Resource.loading(data = null)
        if (email.isEmpty() || password.isEmpty()) {
            _loginStatus.value = Resource.error(context.getString(R.string.fill_out), null)
            return
        }
        viewModelScope.launch {
            val result = repository.login(email, password)
            _loginStatus.value = result
        }
    }

    fun register(email: String, password: String, repeatedPassword: String) {
        _registerStatus.value = Resource.loading(data = null)
        if (email.isEmpty() || password.isEmpty() || repeatedPassword.isEmpty()) {
            _registerStatus.value = Resource.error(context.getString(R.string.fill_out), null)
            return
        }
        if (password != repeatedPassword) {
            _registerStatus.value = Resource.error(context.getString(R.string.passs_dont_match), null)
            return
        }
        viewModelScope.launch {
            val result = repository.register(email, password)
            _registerStatus.value = result
        }
    }

}