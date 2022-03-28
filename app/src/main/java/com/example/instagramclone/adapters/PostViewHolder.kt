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
    private val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
    private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    private val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)
    private val tvCreatedAt: TextView = itemView.findViewById(R.id.tvCreatedAt)
    private val tvLikeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
    private val btnLike: ImageButton = itemView.findViewById(R.id.btnLike)
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
        tvUsername.text = post.user?.username
        tvDescription.text = post.description
        Glide.with(itemView).load(post.image?.url).into(ivPhoto)
        tvCreatedAt.text = post.createdAt
        tvLikeCount.text = "${post.likeCount} likes"
        setButton()
        setButtonImage()
    }

    private fun setButton() {
        btnLike.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                post.toggleLike(object: OnParseActionListener {
                    override fun onParseSuccess() {
                        tvLikeCount.text = "${post.likeCount} likes"
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
            true -> btnLike.setImageResource(R.drawable.ufi_heart_active)
            false -> btnLike.setImageResource(R.drawable.ufi_heart)
        }
    }
}