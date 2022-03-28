package com.example.instagramclone

import com.example.instagramclone.listeners.OnParseActionListener
import com.parse.*

@ParseClassName("Comment")
class Comment() : ParseObject() {
    val user: ParseUser?
        get() {
            return getParseUser(KEY_USER)
        }
    val post: Post
        get() {
            return getParseObject(KEY_POST) as Post
        }
    val text: String?
        get() {
            return getString(KEY_TEXT)
        }

    fun setUser(user: ParseUser) {
        put(KEY_USER, user)
    }
    fun setPost(post: Post) {
        put(KEY_POST, post)
    }
    fun setText(text: String) {
        put(KEY_TEXT, text)
    }

    constructor(user: ParseUser, post: Post, text: String) : this() {
        setUser(user)
        setPost(post)
        setText(text)
    }

    fun submit(onParseActionListener: OnParseActionListener) {
        saveInBackground(object: SaveCallback {
            override fun done(e: ParseException?) {
                if (e == null) {
                    onParseActionListener.onParseSuccess()
                } else {
                    onParseActionListener.onParseException(e)
                }
            }
        })
    }

    companion object {
        const val KEY_TEXT = "text"
        const val KEY_USER = "user"
        const val KEY_POST = "post"

        fun getCommentQuery() : ParseQuery<Comment> {
            val query = ParseQuery.getQuery(Comment::class.java)
            query.orderByDescending("createdAt")
            query.include(KEY_USER)
            query.include(KEY_POST)
            return query
        }

        fun getCommentQueryByPost(post: Post) : ParseQuery<Comment> {
            val query = getCommentQuery()
            query.whereEqualTo(KEY_POST, post)
            return query
        }
    }
}