package com.example.instagramclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.Comment
import com.example.instagramclone.R

class CommentAdapter(
    val comments: ArrayList<Comment>
) : RecyclerView.Adapter<CommentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bindComment(comment)
    }

    override fun getItemCount(): Int {
        return comments.size
    }

    fun add(comment: Comment) {
        comments.add(0, comment)
        notifyItemInserted(0)
    }

    fun addAll(comments: List<Comment>) {
        this.comments.addAll(comments)
        notifyDataSetChanged()
    }
}