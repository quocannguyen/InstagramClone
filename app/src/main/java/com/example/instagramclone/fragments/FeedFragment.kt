package com.example.instagramclone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.*
import com.example.instagramclone.adapters.PostAdapter
import com.example.instagramclone.listeners.OnPassingPostListener
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

open class FeedFragment() : Fragment() {

    lateinit var rvPosts: RecyclerView
    lateinit var postAdapter: PostAdapter
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    var onViewHolderClickListener: OnPassingPostListener? = null

    constructor(onViewHolderClickListener: OnPassingPostListener) : this() {
        this.onViewHolderClickListener = onViewHolderClickListener
    }

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
        postAdapter = PostAdapter(onViewHolderClickListener)
        rvPosts.adapter = postAdapter
        mLayoutManager = getLayoutManager()
        rvPosts.layoutManager = mLayoutManager

        setUpData()
    }

    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(requireContext())
    }

    open fun getParseDataSourceFactory(): ParseDataSourceFactory {
        return ParseDataSourceFactory(null)
    }

    private fun setUpData() {

        // initial page size to fetch can be configured here
        val pagedListConfig =
            PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setPrefetchDistance(10)
                .setInitialLoadSizeHint(20)
                .setPageSize(10)
                .build()

        val sourceFactory = getParseDataSourceFactory()
        val postLiveData = LivePagedListBuilder(sourceFactory, pagedListConfig).build()
        postLiveData.observe(viewLifecycleOwner, object: Observer<PagedList<Post>> {
            override fun onChanged(postPagedList: PagedList<Post>?) {
                Log.d("peter", "FeedFragment setUpData onChanged: ")
                postAdapter.submitList(postPagedList)
            }
        })
    }

    companion object {
        fun newInstance(onViewHolderClickListener: OnPassingPostListener) =
            FeedFragment(onViewHolderClickListener)
    }
}