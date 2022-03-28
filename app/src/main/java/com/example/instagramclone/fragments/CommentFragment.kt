package com.example.instagramclone.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagramclone.Comment
import com.example.instagramclone.Post
import com.example.instagramclone.R
import com.example.instagramclone.adapters.CommentAdapter
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery

class CommentFragment() : Fragment() {
    val comments = ArrayList<Comment>()
    lateinit var rvComments: RecyclerView
    lateinit var commentAdapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("peter", "CommentFragment onCreate: ")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvComments = view.findViewById(R.id.rvComments)
        commentAdapter = CommentAdapter(comments)
        val linearLayoutManager = LinearLayoutManager(requireContext())
        rvComments.adapter = commentAdapter
        rvComments.layoutManager = linearLayoutManager
    }

    fun queryCommentsByPost(post: Post) {
        val query = Comment.getCommentQueryByPost(post)
        query.findInBackground(object: FindCallback<Comment> {
            override fun done(objects: MutableList<Comment>?, e: ParseException?) {
                if (e != null) {
                    Log.e("peter", "CommentFragment queryCommentsByPost done: $e")
                    Toast.makeText(requireContext(), "Error fetching commentss", Toast.LENGTH_LONG).show()
                } else {
                    if (objects != null) {
                        commentAdapter.addAll(objects)
                    }
                }
            }
        })
    }

    companion object {
        fun newInstance(bundle: Bundle) : CommentFragment {
            val commentFragment = CommentFragment()
            commentFragment.arguments = bundle
            return commentFragment
        }
    }
}