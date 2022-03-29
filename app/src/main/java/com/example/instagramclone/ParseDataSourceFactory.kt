package com.example.instagramclone

import androidx.paging.DataSource
import com.parse.ParseUser

class ParseDataSourceFactory(val user: ParseUser?) : DataSource.Factory<Int, Post>() {
    override fun create(): DataSource<Int, Post> {
        val source = ParsePositionalDataSource(user)
        return source
    }
}