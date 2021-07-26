package com.axellsolis.blogapp.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.axellsolis.blogapp.core.Resource
import com.axellsolis.blogapp.domain.posts.PostRepository
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.IllegalArgumentException

class CreatePostViewModel(private val repository: PostRepository) : ViewModel() {

    fun uploadPost(imageBitmap: Bitmap, description: String) = liveData(Dispatchers.IO) {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(repository.uploadPost(imageBitmap, description)))
        } catch (e: Exception) {
            emit(Resource.Failure(e))
        }
    }

}

class CreatePostViewModelFactory(private val repository: PostRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(CreatePostViewModel::class.java)) {
            return CreatePostViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}