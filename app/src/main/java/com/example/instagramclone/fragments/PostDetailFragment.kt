package com.example.instagramclone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.example.instagramclone.*
import com.example.instagramclone.listeners.OnParseActionListener
import com.parse.ParseException
import com.parse.ParseUser

class PostDetailFragment : Fragment() {

    lateinit var tvUsername: TextView
    lateinit var tvLikeCount: TextView
    lateinit var tvDescription: TextView
    lateinit var tvCreatedAt: TextView
    lateinit var ivPhoto: ImageView
    lateinit var btnLikePost: ImageButton
    lateinit var btnSubmitComment: Button
    lateinit var etComment: EditText
    lateinit var post: Post
    lateinit var commentFragment: CommentFragment
    lateinit var ivPostProfileImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        post = arguments?.getParcelable(PARCELABLE_KEY_POST)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvUsername = view.findViewById(R.id.tvPostUsername)
        tvLikeCount = view.findViewById(R.id.tvPostLikeCount)
        tvDescription = view.findViewById(R.id.tvPostDescription)
        tvCreatedAt = view.findViewById(R.id.tvPostCreatedAt)
        ivPhoto = view.findViewById(R.id.ivPostPhoto)
        btnLikePost = view.findViewById(R.id.btnLikePost)
        btnSubmitComment = view.findViewById(R.id.btnSubmitComment)
        etComment = view.findViewById(R.id.etCommentText)
        commentFragment = FragmentManager.findFragment(view.findViewById(R.id.fragComments))
        ivPostProfileImage = view.findViewById(R.id.ivPostProfileImage)

        Glide.with(requireContext()).load(TwitterCloneApplication.getProfileImageUrl(post.user)).into(ivPostProfileImage)
        tvUsername.text = post.user?.username
        tvDescription.text = post.description
        Glide.with(requireContext()).load(post.image?.url).into(ivPhoto)
        tvCreatedAt.text = post.createdAt
        tvLikeCount.text = "${post.likeCount} likes"
        setButtons()
        setButtonImage()
        commentFragment.queryCommentsByPost(post)
    }

    private fun setButtons() {
        btnLikePost.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                UserPostInteraction.toggleLikePost(ParseUser.getCurrentUser(), post, object: OnParseActionListener {
                    override fun onParseSuccess() {
                        tvLikeCount.text = "${post.likeCount} likes"
                        setButtonImage()
                    }
                    override fun onParseException(parseException: ParseException) {
                        Toast.makeText(requireContext(), parseException.message, Toast.LENGTH_SHORT).show()
                        Log.e("peter", "Post update: $parseException", )
                    }
                })
            }
        })
        btnSubmitComment.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                val commentText = etComment.text.toString()
                if (commentText.isNotEmpty()) {
                    val comment = Comment(ParseUser.getCurrentUser(), post, commentText)
                    comment.submit(object: OnParseActionListener {
                        override fun onParseSuccess() {
                            etComment.text = null
                            Toast.makeText(requireContext(), "Comment submitted", Toast.LENGTH_SHORT).show()
                            commentFragment.commentAdapter.add(comment)
                        }
                        override fun onParseException(parseException: ParseException) {
                            Toast.makeText(requireContext(), "Error submitting comment", Toast.LENGTH_SHORT).show()
                            Log.e("peter", "PostDetailFragment setButtons btnSubmitComment done: $parseException", )
                        }
                    })
                } else {
                    Log.e("peter", "PostDetailFragment setButtons btnSubmitComment: commentText.isEmpty()")
                    Toast.makeText(requireContext(), "Comment is empty", Toast.LENGTH_SHORT).show()
                }
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

    companion object {
        fun newInstance(bundle: Bundle) : PostDetailFragment {
            val postDetailFragment = PostDetailFragment()
            postDetailFragment.arguments = bundle
            return postDetailFragment
        }
    }
}