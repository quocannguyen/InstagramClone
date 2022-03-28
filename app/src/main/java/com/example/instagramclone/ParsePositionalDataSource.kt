package com.example.instagramclone

import androidx.paging.PositionalDataSource
import com.parse.ParseQuery

class ParsePositionalDataSource : PositionalDataSource<Post>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Post>) {
        // get basic query
        val query = Post.getPostQuery()

        // Use values passed when PagedList was created.
        query.limit = params.requestedLoadSize
        query.skip = params.requestedStartPosition

        // run queries synchronously since function is called on a background thread
        val count = query.count()
        val posts = query.find()

        // return info back to PagedList
        callback.onResult(posts, params.requestedStartPosition, count)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<Post>) {
        val query = Post.getPostQuery()

        query.limit = params.loadSize
        // fetch the next set from a different offset
        query.skip = params.startPosition

        // synchronous call
        val posts = query.find()

        // return info back to PagedList
        callback.onResult(posts)
    }
}