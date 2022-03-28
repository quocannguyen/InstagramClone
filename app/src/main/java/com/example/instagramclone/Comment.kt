package com.example.instagramclone

import com.example.instagramclone.listeners.OnParseActionListener
import com.parse.*

@ParseClassName("Comment")
class Comment : ParseObject() {
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
    }
}