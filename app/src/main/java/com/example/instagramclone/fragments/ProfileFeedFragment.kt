package com.example.instagramclone.fragments

import android.util.Log
import com.example.instagramclone.PostDataSourceFactory
import com.example.instagramclone.listeners.OnPassingPostListener
import com.parse.ParseUser

class ProfileFeedFragment() : FeedFragment() {

    lateinit var user: ParseUser

    constructor(
        user: ParseUser,
        onViewHolderClickListener: OnPassingPostListener
    ) : this() {
        this.user = user
        this.onViewHolderClickListener = onViewHolderClickListener
        Log.d("peter", "ProfileFeedFragment: ")
    }

    override fun getPostDataSourceFactory(): PostDataSourceFactory {
        return PostDataSourceFactory(user)
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