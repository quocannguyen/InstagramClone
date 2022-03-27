package com.example.instagramclone

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
    private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    private val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)
    private val tvCreatedAt: TextView = itemView.findViewById(R.id.tvCreatedAt)
    private val tvLikeCount: TextView = itemView.findViewById(R.id.tvLikeCount)

    fun bindPost(post: Post) {
        tvUsername.text = post.user?.username
        tvDescription.text = post.description
        Glide.with(itemView).load(post.image?.url).into(ivPhoto)
        tvCreatedAt.text = post.createdAt
        tvLikeCount.text = "${post.likeCount} likes"
        Log.d("peter", "PostViewHolder bindPost: ${post.createdAt}")
    }
}