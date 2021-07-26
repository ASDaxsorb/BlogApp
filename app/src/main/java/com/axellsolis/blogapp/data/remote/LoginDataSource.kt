package com.axellsolis.blogapp.data.remote

import android.graphics.Bitmap
import android.net.Uri
import com.axellsolis.blogapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class LoginDataSource {

    suspend fun signIn(
        email: String,
        password: String
    ): FirebaseUser? {
        val authResult =
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
        return authResult.user
    }

    suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): FirebaseUser? {
        val registerResult =
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        registerResult.user?.let { user ->
            FirebaseFirestore.getInstance().collection("users").document(user.uid).set(
                User(
                    email,
                    username,
                    "image.png"
                )
            ).await()
        }
        return registerResult.user
    }

    suspend fun updateUser(imageBitmap: Bitmap, username: String) {

        val user = FirebaseAuth.getInstance().currentUser
        val reference =
            FirebaseStorage.getInstance().reference.child("${user!!.uid}/profilePicture")
        val byteArray = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)

        val photoUrl =
            reference.putBytes(byteArray.toByteArray()).await().storage.downloadUrl.await()
                .toString()

        val profileUpdates = userProfileChangeRequest {
            displayName = username
            photoUri = Uri.parse(photoUrl)
        }

        user.updateProfile(profileUpdates).await()
    }

}