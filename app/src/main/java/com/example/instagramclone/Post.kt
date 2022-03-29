package com.example.instagramclone

import android.os.Parcelable
import com.example.instagramclone.listeners.OnParseActionListener
import com.parse.*
import kotlinx.parcelize.Parcelize
import java.io.File

@ParseClassName("Post")
@Parcelize
class Post() : ParseObject(), Parcelable {
    val description: String?
        get() {
            return getString(KEY_DESCRIPTION)
        }
    val image: ParseFile?
        get() {
            return getParseFile(KEY_IMAGE)
        }
    val user: ParseUser?
        get() {
            return getParseUser(KEY_USER)
        }
    val createdAt: String
        get() {
            return getCreatedAt().toString()
        }
    val likeCount: Int
        get() {
            return getInt(KEY_LIKE_COUNT)
        }

    fun setDescription(description: String) {
        put(KEY_DESCRIPTION, description)
    }
    fun setImage(image: File?) {
        if (image != null) {
            put(KEY_IMAGE, ParseFile(image))
        }
    }
    fun setUser(user: ParseUser) {
        put(KEY_USER, user)
    }
    fun setLikeCount(likeCount: Int) {
        put(KEY_LIKE_COUNT, likeCount)
    }

    constructor(description: String, user: ParseUser, photoFile: File?) : this() {
        setDescription(description)
        setUser(user)
        setImage(photoFile)
    }

    override fun toString(): String {
        return "Post(id=$objectId)"
    }

    // Send a Post object to Parse server
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

    fun update(description: String?, image: File?, likeCount: Int?, onParseActionListener: OnParseActionListener) {
        // Update the fields we want to
        if (description != null) {
            setDescription(description)
        }
        if (image != null) {
            setImage(image)
        }
        if (likeCount != null) {
            setLikeCount(likeCount)
        }

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

    override fun equals(other: Any?): Boolean {
        if (other is Post) {
            val otherPost = other.fetchIfNeeded<Post>()
            return description == otherPost.description &&
                    user?.objectId == otherPost.user?.objectId &&
                    image?.data.contentEquals(otherPost.image?.data)
        }
        return super.equals(other)
    }

    companion object {
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
        const val KEY_LIKE_COUNT = "likeCount"

        fun getPostQuery(user: ParseUser?) : ParseQuery<Post> {
            val query = ParseQuery.getQuery(Post::class.java)
            query.orderByDescending("createdAt")
            query.include(KEY_USER)
            if (user != null) {
                query.whereEqualTo(KEY_USER, user)
            }
            return query
        }
    }
}