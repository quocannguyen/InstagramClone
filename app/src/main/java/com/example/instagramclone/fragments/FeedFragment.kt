package com.example.instagramclone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    val posts = ArrayList<Post>()
//    lateinit var postList : PagedList<Post>
    lateinit var rvPosts: RecyclerView
    lateinit var postAdapter: PostAdapter
    lateinit var mLayoutManager: RecyclerView.LayoutManager
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

        // initial page size to fetch can be configured here
        val pagedListConfig =
            PagedList.Config.Builder().setEnablePlaceholders(true)
                .setPrefetchDistance(10)
                .setInitialLoadSizeHint(20)
                .setPageSize(10).build()

        val sourceFactory = ParseDataSourceFactory()
//        postList = LivePagedListBuilder(sourceFactory, pagedListConfig).build()
//        postList.observe(viewLifecycleOwner, object: Observer<PagedList<Post>> {
//            override fun onChanged(t: PagedList<Post>?) {
//                adapter.submitList(posts)
//            }
//
//        })

        rvPosts = view.findViewById(R.id.rvPosts)
        postAdapter = PostAdapter(posts, onViewHolderClickListener)
        rvPosts.adapter = postAdapter
        mLayoutManager = getLayoutManager()
        rvPosts.layoutManager = mLayoutManager

        queryPosts()
    }

    // Query for all posts on server
    open fun queryPosts() {
        val query = getPostQuery()
        findPostQueryInBackground(query)
    }

    fun findPostQueryInBackground(query: ParseQuery<Post>) {
        // Find all Post objects
        query.findInBackground(object: FindCallback<Post> {
            override fun done(objects: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e("peter", "FeedFragment queryPosts done: $e")
                    Toast.makeText(requireContext(), "Error fetching posts", Toast.LENGTH_LONG).show()
                } else {
                    if (objects != null) {
                        postAdapter.addAll(objects)
                    }
                }
            }
        })
    }

    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(requireContext())
    }

    companion object {
        fun newInstance(onViewHolderClickListener: OnPassingPostListener) =
            FeedFragment(onViewHolderClickListener)

        fun getPostQuery(): ParseQuery<Post> {
            // Specify which class to query
            val query = Post.getPostQuery()
            query.include(Post.KEY_USER)
            query.limit = 20
            return query
        }
    }
}