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
import com.example.instagramclone.TwitterCloneApplication
import com.example.instagramclone.UserPostInteraction
import com.example.instagramclone.listeners.OnPassingPostListener
import com.example.instagramclone.listeners.OnParseActionListener
import com.example.instagramclone.listeners.OnPassingUserListener
import com.parse.ParseException
import com.parse.ParseUser

class PostViewHolder(
    itemView: View,
    val onViewHolderClickListener: OnPassingPostListener?,
    val onProfileClickListener: OnPassingUserListener?
) : RecyclerView.ViewHolder(itemView) {

    lateinit var post: Post
    private val tvPostUsername: TextView = itemView.findViewById(R.id.tvPostUsername)
    private val tvPostDescription: TextView = itemView.findViewById(R.id.tvPostDescription)
    private val ivPostPhoto: ImageView = itemView.findViewById(R.id.ivPostPhoto)
    private val tvPostCreatedAt: TextView = itemView.findViewById(R.id.tvPostCreatedAt)
    private val tvPostLikeCount: TextView = itemView.findViewById(R.id.tvPostLikeCount)
    private val btnLikePost: ImageButton = itemView.findViewById(R.id.btnLikePost)
    private val ivPostProfileImage: ImageView = itemView.findViewById(R.id.ivPostProfileImage)

    init {
        itemView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                onViewHolderClickListener?.onPostPassed(post)
            }
        })
        tvPostUsername.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                launchProfile()
            }
        })
        ivPostProfileImage.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                launchProfile()
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
        Glide.with(itemView).load(TwitterCloneApplication.getProfileImageUrl(post.user)).into(ivPostProfileImage)

        setButton()
        setButtonImage()
    }

    private fun setButton() {
        btnLikePost.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                UserPostInteraction.toggleLikePost(ParseUser.getCurrentUser(), post, object: OnParseActionListener {
                    override fun onParseSuccess() {
                        tvPostLikeCount.text = "${post.likeCount} likes"
                        setButtonImage()
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
        val userPostInteraction = UserPostInteraction.interactionsByUser[post.objectId]
        if (userPostInteraction != null) {
            when (userPostInteraction.liked) {
                true -> btnLikePost.setImageResource(R.drawable.ufi_heart_active)
                false -> btnLikePost.setImageResource(R.drawable.ufi_heart)
            }
        }
    }

    private fun launchProfile() {
        onProfileClickListener?.onUserPassed(post.user!!)
    }
}