package com.axellsolis.blogapp.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.axellsolis.blogapp.core.Resource
import com.axellsolis.blogapp.domain.auth.LoginRepository
import kotlinx.coroutines.Dispatchers
import java.lang.IllegalArgumentException

class RegisterViewModel(private val repository: LoginRepository) : ViewModel() {

    fun signUp(email: String, username: String, password: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.signUp(email, password, username)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

    fun updateProfile(imageBitmap: Bitmap, username: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.updateProfile(imageBitmap, username)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

}

class RegisterViewModelFactory(private val repository: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}