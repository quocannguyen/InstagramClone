package com.example.instagramclone

import androidx.paging.DataSource

class ParseDataSourceFactory : DataSource.Factory<Int, Post>() {
    override fun create(): DataSource<Int, Post> {
        val source = ParsePositionalDataSource()
        return source
    }
}