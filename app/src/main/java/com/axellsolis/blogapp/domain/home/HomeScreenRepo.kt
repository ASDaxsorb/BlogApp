package com.axellsolis.blogapp.domain.home

import com.axellsolis.blogapp.core.Resource
import com.axellsolis.blogapp.data.Post

interface HomeScreenRepo {
    suspend fun getLatestPost(): Resource<List<Post>>
}