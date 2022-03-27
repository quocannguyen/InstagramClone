package com.example.instagramclone.fragments

import android.util.Log
import android.widget.Toast
import com.example.instagramclone.Post
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment : FeedFragment() {

    override fun queryPosts() {
        // Specify which class to query
        val query = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        query.orderByDescending("createdAt")
        query.limit = 20
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        // Find all Post objects
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e("peter", "FeedFragment queryPosts done: $e")
                    Toast.makeText(requireContext(), "Error fetching posts", Toast.LENGTH_LONG).show()
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            Log.i("peter", "FeedFragment queryPosts done: $post")
                        }
                        adapter.addAll(posts)
                    }
                }
            }
        })
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}