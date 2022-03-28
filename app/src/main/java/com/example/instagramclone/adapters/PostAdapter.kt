package com.example.instagramclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.instagramclone.Post
import com.example.instagramclone.R
import com.example.instagramclone.listeners.OnPassingPostListener

class PostAdapter(
    private val posts: ArrayList<Post>,
    private val onViewHolderClickListener: OnPassingPostListener?,
    private val onCommentButtonClickListener: OnPassingPostListener?
) : ListAdapter<Post, PostViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view, onViewHolderClickListener, onCommentButtonClickListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bindPost(post)
    }

    fun clear() {
        posts.clear()
        submitList(this.posts)
    }

    fun addAll(posts: List<Post>) {
        this.posts.addAll(posts)
        submitList(this.posts)
    }

    companion object {

        val DIFF_CALLBACK: DiffUtil.ItemCallback<Post> = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.objectId == newItem.objectId
            }

            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
                return oldItem.description == newItem.description &&
                        oldItem.user?.getString("objectID") == newItem.user?.getString("objectID") &&
                        oldItem.image?.data.contentEquals(newItem.image?.data)
            }
        }
    }
}