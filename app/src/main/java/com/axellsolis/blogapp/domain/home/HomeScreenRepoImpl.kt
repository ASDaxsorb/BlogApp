package com.axellsolis.blogapp.domain.home

import com.axellsolis.blogapp.core.Resource
import com.axellsolis.blogapp.data.Post
import com.axellsolis.blogapp.data.remote.HomeScreenDataSource

class HomeScreenRepoImpl(val dataSource: HomeScreenDataSource) : HomeScreenRepo {

    override suspend fun getLatestPost(): Resource<List<Post>> = dataSource.getLatestPost()
}