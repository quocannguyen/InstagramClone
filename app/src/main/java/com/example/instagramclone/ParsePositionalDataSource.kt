package com.example.instagramclone

import android.util.Log
import androidx.paging.PositionalDataSource
import com.parse.ParseQuery
import com.parse.ParseUser

class ParsePositionalDataSource(val user: ParseUser?) : PositionalDataSource<Post>() {

    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<Post>) {
        Log.d("peter", "ParsePositionalDataSource loadInitial: ")
        // get basic query
        val query = Post.getPostQuery(user)

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
        Log.d("peter", "ParsePositionalDataSource loadRange: ")
        val query = Post.getPostQuery(user)

        // fetch the next set from a different offset
        query.skip = params.startPosition
        query.limit = params.loadSize

        // synchronous call
        val posts = query.find()

        // return info back to PagedList
        callback.onResult(posts)
    }
}