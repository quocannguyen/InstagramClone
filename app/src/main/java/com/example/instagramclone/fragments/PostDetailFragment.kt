package com.example.instagramclone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.instagramclone.PARCELABLE_KEY_POST
import com.example.instagramclone.Post
import com.example.instagramclone.R
import com.example.instagramclone.listeners.OnParseActionListener
import com.parse.ParseException

class PostDetailFragment : Fragment() {

    lateinit var tvUsername: TextView
    lateinit var tvLikeCount: TextView
    lateinit var tvDescription: TextView
    lateinit var tvCreatedAt: TextView
    lateinit var ivPhoto: ImageView
    lateinit var btnLike: ImageButton
    lateinit var post: Post

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

        tvUsername = view.findViewById(R.id.tvUsername)
        tvLikeCount = view.findViewById(R.id.tvLikeCount)
        tvDescription = view.findViewById(R.id.tvDescription)
        tvCreatedAt = view.findViewById(R.id.tvCreatedAt)
        ivPhoto = view.findViewById(R.id.ivPhoto)
        btnLike = view.findViewById(R.id.btnLike)

        tvUsername.text = post.user?.username
        tvDescription.text = post.description
        Glide.with(requireContext()).load(post.image?.url).into(ivPhoto)
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
                    }
                    override fun onParseException(parseException: ParseException) {
                        Toast.makeText(requireContext(), parseException.message, Toast.LENGTH_SHORT).show()
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

    companion object {
        fun newInstance(bundle: Bundle) : PostDetailFragment {
            val postDetailFragment = PostDetailFragment()
            postDetailFragment.arguments = bundle
            return postDetailFragment
        }
    }
}