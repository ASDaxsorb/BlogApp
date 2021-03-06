package com.axellsolis.blogapp.data.remote

import com.axellsolis.blogapp.core.Resource
import com.axellsolis.blogapp.data.Post
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class HomeScreenDataSource {
    suspend fun getLatestPost(): Resource<List<Post>> {
        val postList = mutableListOf<Post>()
        val querySnapshot = FirebaseFirestore.getInstance().collection("posts").get().await()

        for (post in querySnapshot.documents) {
            post.toObject(Post::class.java)?.let {
                postList.add(it)
            }
        }

        return Resource.Success(postList)
    }
}