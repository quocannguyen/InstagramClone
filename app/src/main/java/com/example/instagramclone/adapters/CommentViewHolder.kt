package com.example.instagramclone.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.Comment
import com.example.instagramclone.R

class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    lateinit var comment: Comment
    private var tvCommentUsername: TextView = itemView.findViewById(R.id.tvCommentUsername)
    private var tvCommentText: TextView = itemView.findViewById(R.id.tvCommentText)

    fun bindComment(comment: Comment) {
        this.comment = comment
        tvCommentUsername.text = comment.user?.username
        tvCommentText.text = comment.text
    }
}