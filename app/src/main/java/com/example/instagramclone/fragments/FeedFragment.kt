package com.example.instagramclone.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.instagramclone.*
import com.example.instagramclone.adapters.PostAdapter
import com.example.instagramclone.listeners.OnPassingPostListener
import com.example.instagramclone.listeners.OnPassingUserListener


open class FeedFragment() : Fragment() {

    lateinit var rvPosts: RecyclerView
    lateinit var postAdapter: PostAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var mLayoutManager: RecyclerView.LayoutManager
    var onViewHolderClickListener: OnPassingPostListener? = null
    var onProfileClickListener: OnPassingUserListener? = null
    lateinit var sourceFactory: PostDataSourceFactory

    constructor(
        onViewHolderClickListener: OnPassingPostListener,
        onProfileClickListener: OnPassingUserListener
    ) : this() {
        this.onViewHolderClickListener = onViewHolderClickListener
        this.onProfileClickListener = onProfileClickListener
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
        swipeContainer = view.findViewById(R.id.swipeContainer)
        sourceFactory = getPostDataSourceFactory()

        setUpAdapter()
        setUpData()
        setUpSwipeRefresh()
    }

    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(requireContext())
    }

    open fun getPostDataSourceFactory(): PostDataSourceFactory {
        return PostDataSourceFactory(null)
    }

    private fun setUpAdapter() {
        postAdapter = PostAdapter(onViewHolderClickListener, onProfileClickListener)
        rvPosts.adapter = postAdapter
        mLayoutManager = getLayoutManager()
        rvPosts.layoutManager = mLayoutManager
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

        val postLiveData = LivePagedListBuilder(sourceFactory, pagedListConfig).build()
        postLiveData.observe(viewLifecycleOwner, object: Observer<PagedList<Post>> {
            override fun onChanged(postPagedList: PagedList<Post>?) {
                Log.d("peter", "FeedFragment setUpData onChanged: $postPagedList")
                postAdapter.submitList(postPagedList)
                if (swipeContainer.isRefreshing) {
                    swipeContainer.isRefreshing = false
                    rvPosts.scrollToPosition(0)
                }
                swipeContainer.isRefreshing = false;
            }
        })
    }

    private fun setUpSwipeRefresh() {
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener {
            // Your code to refresh the list here.
            // Make sure you call swipeContainer.setRefreshing(false)
            // once the network request has completed successfully.
            sourceFactory.postLiveData.value?.invalidate()
        }
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    companion object {
        fun newInstance(
            onViewHolderClickListener: OnPassingPostListener,
            onProfileClickListener: OnPassingUserListener
        ) = FeedFragment(onViewHolderClickListener, onProfileClickListener)
    }
}