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
    val post: Post
        get() {
            return getParseObject(KEY_POST) as Post
        }
    val liked: Boolean
        get() {
            return getBoolean(KEY_LIKED)
        }

    fun setUser(user: ParseUser) {
        put(KEY_USER, user)
    }
    fun setPost(post: Post) {
        put(KEY_POST, post)
    }
    fun setLiked(liked: Boolean) {
        put(KEY_LIKED, liked)
    }

    constructor(user: ParseUser, post: Post, liked: Boolean) : this() {
        setUser(user)
        setPost(post)
        setLiked(liked)
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
        val query = ParseQuery.getQuery(UserPostInteraction::class.java)
        query.getInBackground(objectId, object: GetCallback<UserPostInteraction> {
            override fun done(userPostInteraction: UserPostInteraction, e: ParseException?) {
                if (e == null) {
                    val post = userPostInteraction.post
                    if (userPostInteraction.liked && !liked) {
                        post.update(null, null, post.fetchIfNeeded<Post>().likeCount + 1, onParseActionListener)
                    } else if (!userPostInteraction.liked && liked) {
                        post.update(null, null, post.fetchIfNeeded<Post>().likeCount - 1, onParseActionListener)
                    }

                    userPostInteraction.setLiked(liked)
                    this@UserPostInteraction.setLiked(liked)
                    userPostInteraction.saveInBackground()
                } else {
                    Log.e("peter", "UserPostInteraction update getInBackground done: $e", )
                    onParseActionListener.onParseException(e)
                }
            }
        })
    }

    class Key(user: ParseUser, post: Post) : UserPostInteractionKey(user, post) {}

    companion object {
        const val KEY_USER = "user"
        const val KEY_POST = "post"
        const val KEY_LIKED = "liked"

        val userPostInteractions = HashMap<Key, UserPostInteraction>()

        fun initializeHashMap(user: ParseUser?) {
            val query = getUserPostInteractionQuery(user, null)
            query.findInBackground(object: FindCallback<UserPostInteraction> {
                override fun done(objects: MutableList<UserPostInteraction>?, e: ParseException?) {
                    if (e == null) {
                        if (objects != null) {
                            for (userPostInteraction in objects) {
                                if (userPostInteraction.user != null) {
                                    userPostInteractions[Key(
                                        userPostInteraction.user!!,
                                        userPostInteraction.post
                                    )] = userPostInteraction
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
            if (userPostInteractions.containsKey(Key(user, post))) {
                val userPostInteraction = userPostInteractions[Key(user, post)]
                userPostInteraction?.update(!userPostInteraction.liked, onParseActionListener)
            } else {
                val userPostInteraction = UserPostInteraction(user, post, false)
                userPostInteraction.submit(object: OnParseActionListener {
                    override fun onParseSuccess() {
                        userPostInteraction.update(true, onParseActionListener)
                        userPostInteractions[Key(user, post)] = userPostInteraction
                    }
                    override fun onParseException(parseException: ParseException) {
                        Log.e("peter", "UserPostInteraction toggleLikePost onParseException: $parseException", )
                    }
                })
            }
        }
    }
}