package com.example.instagramclone.fragments

import com.example.instagramclone.Post
import com.example.instagramclone.listeners.OnPassingPostListener
import com.parse.ParseUser

class ProfileFeedFragment() : FeedFragment() {

    constructor(onViewHolderClickListener: OnPassingPostListener) : this() {
        this.onViewHolderClickListener = onViewHolderClickListener
    }

    override fun queryPosts() {
        val query = getPostQuery()
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
        findPostQueryInBackground(query)
    }

//    override fun getLayoutManager(): RecyclerView.LayoutManager {
//        return GridLayoutManager(requireContext(), 3)
//    }

    companion object {
        fun newInstance(onViewHolderClickListener: OnPassingPostListener) =
            ProfileFeedFragment(onViewHolderClickListener)
    }
}