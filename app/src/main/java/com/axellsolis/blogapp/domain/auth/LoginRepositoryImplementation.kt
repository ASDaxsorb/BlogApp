package com.axellsolis.blogapp.domain.auth

import android.graphics.Bitmap
import com.axellsolis.blogapp.data.remote.LoginDataSource
import com.google.firebase.auth.FirebaseUser

class LoginRepositoryImplementation(private val dataSource: LoginDataSource) : LoginRepository {

    override suspend fun signIn(
        email: String,
        password: String
    ): FirebaseUser? = dataSource.signIn(email, password)

    override suspend fun signUp(
        email: String,
        password: String,
        username: String
    ): FirebaseUser? = dataSource.signUp(email, username, password)

    override suspend fun updateProfile(imageBitmap: Bitmap, username: String) =
        dataSource.updateUser(imageBitmap, username)
}