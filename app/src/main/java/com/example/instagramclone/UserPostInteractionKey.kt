package com.example.instagramclone

import com.parse.ParseUser
import java.util.*

open class UserPostInteractionKey(
    val user: ParseUser,
    val post: Post
) {
    override fun equals(other: Any?): Boolean {
        if (other is UserPostInteractionKey) {
            return this.user == other.user && this.post == other.post
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return Objects.hash(user, post)
    }
}