package com.example.instagramclone.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.Post
import com.example.instagramclone.PostAdapter
import com.example.instagramclone.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

open class FeedFragment : Fragment() {

    val posts = ArrayList<Post>()
    lateinit var rvPosts: RecyclerView
    lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPosts = view.findViewById(R.id.rvPosts)
        adapter = PostAdapter(posts)
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

    // Query for all posts on server
    open fun queryPosts() {
        // Specify which class to query
        val query = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        query.orderByDescending("createdAt")
        query.limit = 20
        // Find all Post objects
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e("peter", "FeedFragment queryPosts done: $e")
                    Toast.makeText(requireContext(), "Error fetching posts", Toast.LENGTH_LONG).show()
                } else {
                    if (posts != null) {
                        adapter.addAll(posts)
                    }
                }
            }
        })
    }

    companion object {
        fun newInstance() =
            FeedFragment()
    }
}