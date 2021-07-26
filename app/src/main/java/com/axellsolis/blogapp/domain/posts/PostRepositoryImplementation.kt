package com.axellsolis.blogapp.domain.posts

import android.graphics.Bitmap
import com.axellsolis.blogapp.data.remote.PostDataSource

class PostRepositoryImplementation(private val datasource: PostDataSource) : PostRepository {

    override suspend fun uploadPost(
        imageBitmap: Bitmap,
        description: String
    ) = datasource.uploadPost(imageBitmap, description)

}