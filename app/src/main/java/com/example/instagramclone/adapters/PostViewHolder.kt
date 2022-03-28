package com.example.instagramclone.adapters

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.Post
import com.example.instagramclone.R
import com.example.instagramclone.listeners.OnPassingPostListener
import com.example.instagramclone.listeners.OnParseActionListener
import com.parse.ParseException

class PostViewHolder(
    itemView: View,
    val onViewHolderClickListener: OnPassingPostListener?,
    val onCommentButtonClickListener: OnPassingPostListener?
) : RecyclerView.ViewHolder(itemView) {

    lateinit var post: Post
    private val tvPostUsername: TextView = itemView.findViewById(R.id.tvPostUsername)
    private val tvPostDescription: TextView = itemView.findViewById(R.id.tvPostDescription)
    private val ivPostPhoto: ImageView = itemView.findViewById(R.id.ivPostPhoto)
    private val tvPostCreatedAt: TextView = itemView.findViewById(R.id.tvPostCreatedAt)
    private val tvPostLikeCount: TextView = itemView.findViewById(R.id.tvPostLikeCount)
    private val btnLikePost: ImageButton = itemView.findViewById(R.id.btnLikePost)
    private val btnComment: ImageButton = itemView.findViewById(R.id.btnComment)

    init {
        itemView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                onViewHolderClickListener?.onPostPassed(post)
            }
        })
    }

    fun bindPost(post: Post) {
        this.post = post
        tvPostUsername.text = post.user?.username
        tvPostDescription.text = post.description
        Glide.with(itemView).load(post.image?.url).into(ivPostPhoto)
        tvPostCreatedAt.text = post.createdAt
        tvPostLikeCount.text = "${post.likeCount} likes"
        setButton()
        setButtonImage()
    }

    private fun setButton() {
        btnLikePost.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                post.toggleLike(object: OnParseActionListener {
                    override fun onParseSuccess() {
                        tvPostLikeCount.text = "${post.likeCount} likes"
                        setButtonImage()
                        this@PostViewHolder.bindingAdapter?.notifyItemChanged(this@PostViewHolder.bindingAdapterPosition)
                    }
                    override fun onParseException(parseException: ParseException) {
                        Toast.makeText(itemView.context, parseException.message, Toast.LENGTH_SHORT).show()
                        Log.e("peter", "Post update: $parseException", )
                    }
                })
            }
        })
        btnComment.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                onCommentButtonClickListener?.onPostPassed(post)
            }
        })
    }

    private fun setButtonImage() {
        when (post.liked) {
            true -> btnLikePost.setImageResource(R.drawable.ufi_heart_active)
            false -> btnLikePost.setImageResource(R.drawable.ufi_heart)
        }
    }
}