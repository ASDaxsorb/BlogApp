package com.axellsolis.blogapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.axellsolis.blogapp.core.Resource
import com.axellsolis.blogapp.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class HomeScreenViewModel(private val repository: HomeScreenRepo) : ViewModel() {

    fun getPosts() = liveData(Dispatchers.IO) {

        emit(Resource.Loading())
        try {
            emit(repository.getLatestPost())
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }

    }

}

class HomeScreenViewModelFactory(private val repository: HomeScreenRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            return HomeScreenViewModel(repository) as T
        }
        throw Exception("Unknown ViewModel class")
    }
}

