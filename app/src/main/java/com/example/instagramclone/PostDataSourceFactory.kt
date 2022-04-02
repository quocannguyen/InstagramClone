package com.example.instagramclone

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.parse.ParseUser


class PostDataSourceFactory(val user: ParseUser?) : DataSource.Factory<Int, Post>() {

    // Use to hold a reference to the
    lateinit var postLiveData: MutableLiveData<PostPositionalDataSource>

    override fun create(): DataSource<Int, Post> {
        val dataSource = PostPositionalDataSource(user)

        // Keep reference to the data source with a MutableLiveData reference
        postLiveData = MutableLiveData()
        postLiveData.postValue(dataSource)

        return dataSource
    }
}