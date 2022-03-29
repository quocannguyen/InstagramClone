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
        val query = ParseQuery.getQuery(Post::class.java)

        // Retrieve the object by id
        query.getInBackground(objectId, object: GetCallback<Post> {
            override fun done(post: Post, e: ParseException?) {
                if (e == null) {
                    // Update the fields we want to
                    if (description != null) {
                        post.put(KEY_DESCRIPTION, description)
                        this@Post.setDescription(description)
                    }
                    if (image != null) {
                        post.put(KEY_IMAGE, ParseFile(image))
                        this@Post.setImage(image)
                    }
                    if (likeCount != null) {
                        post.setLikeCount(likeCount)
                        this@Post.setLikeCount(likeCount)
                    }

                    post.saveInBackground()
                    onParseActionListener.onParseSuccess()
                } else {
                    // something went wrong
                    onParseActionListener.onParseException(e)
                }
            }
        })
    }

    companion object {
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
        const val KEY_LIKE_COUNT = "likeCount"

        fun getPostQuery() : ParseQuery<Post> {
            val query = ParseQuery.getQuery(Post::class.java)
            query.orderByDescending("createdAt")
            return query
        }
    }
}