package com.example.instagramclone

import android.util.Log
import com.example.instagramclone.listeners.OnParseActionListener
import com.parse.*

@ParseClassName("UserPostInteraction")
class UserPostInteraction() : ParseObject() {

    val user: ParseUser?
        get() {
            return getParseUser(KEY_USER)
        }
    var post: Post
        get() {
            return getParseObject(KEY_POST) as Post
        }
        set(post) {
            put(KEY_POST, post)
        }
    val liked: Boolean
        get() {
            return getBoolean(KEY_LIKED)
        }

    fun setUser(user: ParseUser) {
        put(KEY_USER, user)
    }
    fun setLiked(liked: Boolean) {
        put(KEY_LIKED, liked)
    }

    constructor(user: ParseUser, post: Post, liked: Boolean) : this() {
        setUser(user)
        this.post = post
        setLiked(liked)
    }

    override fun toString(): String {
        return "UserPostInteraction(id=$objectId)"
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

    fun update(liked: Boolean, onParseActionListener: OnParseActionListener) {
        if (this.liked && !liked) {
            post?.update(null, null, post?.likeCount?.minus(1), onParseActionListener)
        } else if (!this.liked && liked) {
            post?.update(null, null, post?.likeCount?.plus(1), onParseActionListener)
        }

        setLiked(liked)
        saveInBackground()
    }

    companion object {
        const val KEY_USER = "user"
        const val KEY_POST = "post"
        const val KEY_LIKED = "liked"

        // Key is post.objectId
        val interactionsByUser = HashMap<String, UserPostInteraction>()

        fun initializeHashMap(user: ParseUser?) {
            interactionsByUser.clear()
            val query = getUserPostInteractionQuery(user, null)
            query.findInBackground(object: FindCallback<UserPostInteraction> {
                override fun done(objects: MutableList<UserPostInteraction>?, e: ParseException?) {
                    if (e == null) {
                        if (objects != null) {
                            for (userPostInteraction in objects) {
                                val post = userPostInteraction.post
                                if (post != null) {
                                    interactionsByUser[post.objectId] = userPostInteraction
                                }
                            }
                        }
                    } else {
                        Log.e("peter", "UserPostInteraction initializeHashMap done: $e", )
                    }
                }
            })
        }

        fun getUserPostInteractionQuery(user: ParseUser?, post: Post?) : ParseQuery<UserPostInteraction> {
            val query = ParseQuery.getQuery(UserPostInteraction::class.java)
            query.orderByDescending("createdAt")
            query.include(Comment.KEY_USER)
            query.include(Comment.KEY_POST)
            if (user != null) {
                query.whereEqualTo(KEY_USER, user)
            }
            if (post != null) {
                query.whereEqualTo(KEY_POST, post)
            }
            return query
        }

        fun toggleLikePost(user: ParseUser, post: Post, onParseActionListener: OnParseActionListener) {
            if (interactionsByUser.containsKey(post.objectId)) {
                val userPostInteraction = interactionsByUser[post.objectId]
                // Re-assigning post
                userPostInteraction?.post = post
                userPostInteraction?.update(!userPostInteraction.liked, onParseActionListener)
            } else {
                val userPostInteraction = UserPostInteraction(user, post, false)
                userPostInteraction.submit(object: OnParseActionListener {
                    override fun onParseSuccess() {
                        userPostInteraction.update(true, onParseActionListener)
                        interactionsByUser[post.objectId] = userPostInteraction
                    }
                    override fun onParseException(parseException: ParseException) {
                        Log.e("peter", "UserPostInteraction toggleLikePost onParseException: $parseException", )
                    }
                })
            }
        }
    }
}