package com.example.instagramclone.fragments

import com.example.instagramclone.ParseDataSourceFactory
import com.example.instagramclone.Post
import com.example.instagramclone.listeners.OnPassingPostListener
import com.example.instagramclone.listeners.OnPassingUserListener
import com.parse.ParseUser

class ProfileFeedFragment() : FeedFragment() {

    lateinit var user: ParseUser

    constructor(
        user: ParseUser,
        onViewHolderClickListener: OnPassingPostListener
    ) : this() {
        this.user = user
        this.onViewHolderClickListener = onViewHolderClickListener
    }

    override fun getParseDataSourceFactory(): ParseDataSourceFactory {
        return ParseDataSourceFactory(user)
    }

//    override fun getLayoutManager(): RecyclerView.LayoutManager {
//        return GridLayoutManager(requireContext(), 3)
//    }

    companion object {
        fun newInstance(
            user: ParseUser,
            onViewHolderClickListener: OnPassingPostListener
        ) = ProfileFeedFragment(user, onViewHolderClickListener)
    }
}