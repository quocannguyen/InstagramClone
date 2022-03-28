package com.example.instagramclone

import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instagramclone.listeners.OnParseActionListener
import com.parse.ParseException

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var post: Post
    private val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
    private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    private val ivPhoto: ImageView = itemView.findViewById(R.id.ivPhoto)
    private val tvCreatedAt: TextView = itemView.findViewById(R.id.tvCreatedAt)
    private val tvLikeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
    val btnLike = itemView.findViewById<ImageButton>(R.id.btnLike)

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
                        this@PostViewHolder.bindingAdapter?.notifyDataSetChanged()
                    }
                    override fun onParseException(parseException: ParseException) {
                        Toast.makeText(itemView.context, parseException.message, Toast.LENGTH_SHORT).show()
                        Log.e("peter", "Post update: $parseException", )
                    }
                })
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