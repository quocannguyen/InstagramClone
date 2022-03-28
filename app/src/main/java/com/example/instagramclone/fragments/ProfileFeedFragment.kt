package com.example.instagramclone.fragments

import com.example.instagramclone.Post
import com.parse.ParseUser

class ProfileFeedFragment() : FeedFragment() {

    override fun queryPosts() {
        val query = getPostQuery()
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        findPostQueryInBackground(query)
    }

    companion object {
        fun newInstance() = ProfileFeedFragment()
    }
}