package com.axellsolis.blogapp.core

import java.lang.Exception

sealed class Resource<out T> {
    class Loading<out T> : Resource<T>()
    class Success<out T> (val data: T) : Resource<T>()
    class Failure(val exception: Exception) : Resource<Nothing>()
}