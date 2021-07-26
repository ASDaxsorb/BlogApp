package com.axellsolis.blogapp.data

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class Post(
    val profilePicture: String = "",
    val profileName: String = "",
    @ServerTimestamp
    val createdAt: Date? = null,
    val postImage: String = "",
    val postDescription: String = "",
    val uid: String = ""
)