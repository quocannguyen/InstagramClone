package com.example.instagramclone.adapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.Comment
import com.example.instagramclone.R
import com.example.instagramclone.TwitterCloneApplication

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var comment: Comment
    private var tvCommentUsername: TextView = itemView.findViewById(R.id.tvCommentUsername)
    private var tvCommentText: TextView = itemView.findViewById(R.id.tvCommentText)
    private var ivCommentProfileImage: ImageView = itemView.findViewById(R.id.ivCommentProfileImage)

    fun bindComment(comment: Comment) {
        this.comment = comment
        tvCommentUsername.text = comment.user?.username
        tvCommentText.text = comment.text
        Glide.with(itemView).load(TwitterCloneApplication.getProfileImageUrl(comment.user)).into(ivCommentProfileImage)
    }
}