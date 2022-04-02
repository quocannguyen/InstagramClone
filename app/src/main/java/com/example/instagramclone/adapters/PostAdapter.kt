package com.example.instagramclone.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.instagramclone.Post
import com.example.instagramclone.R
import com.example.instagramclone.listeners.OnPassingPostListener
import com.example.instagramclone.listeners.OnPassingUserListener

class PostAdapter(
    private val onViewHolderClickListener: OnPassingPostListener?,
    private val onProfileClickListener: OnPassingUserListener?
) : PagedListAdapter<Post, PostViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view, onViewHolderClickListener, onProfileClickListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bindPost(post!!)
    }

    companion object {

        val DIFF_CALLBACK: DiffUtil.ItemCallback<Post> = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                Log.d("peter", "areItemsTheSame: ${(oldItem.objectId == newItem.objectId)}")
                return oldItem.objectId == newItem.objectId
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                Log.d("peter", "areContentsTheSame: ${(oldItem == newItem)}")
                return oldItem == newItem
            }
        }
    }
}