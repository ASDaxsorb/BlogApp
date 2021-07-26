package com.axellsolis.blogapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.axellsolis.blogapp.core.Resource
import com.axellsolis.blogapp.domain.auth.LoginRepository
import kotlinx.coroutines.Dispatchers

class LoginScreenViewModel(private val repository: LoginRepository) : ViewModel() {

    fun signIn(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.signIn(email, password)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }
}

class LoginScreenViewModelFactory(
    private val repository: LoginRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginScreenViewModel(repository) as T
        }
        throw Exception("Unknown ViewModel Class")
    }
}