package com.axellsolis.blogapp.data.remote

import android.graphics.Bitmap
import com.axellsolis.blogapp.data.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.*

class PostDataSource {

    suspend fun uploadPost(imageBitmap: Bitmap, description: String) {

        val user = Firebase.auth.currentUser
        val postsPath =
            FirebaseFirestore.getInstance().collection("posts")
        val imageUrl = uploadBitmap(imageBitmap)

        user?.let {
            val post = Post(
                profileName = user.displayName.toString(),
                profilePicture = user.photoUrl.toString(),
                postDescription = description,
                postImage = imageUrl,
                uid = user.uid
            )
            postsPath.add(post).await()
        }

    }

    private suspend fun uploadBitmap(bitmap: Bitmap): String {

        val user = FirebaseAuth.getInstance().currentUser
        val postUuid = UUID.randomUUID().toString()
        val postsPath =
            FirebaseStorage.getInstance().reference.child("${user?.uid}/posts/$postUuid")

        val byteArray = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray)

        return postsPath.putBytes(byteArray.toByteArray()).await().storage.downloadUrl.await()
            .toString()
    }

}