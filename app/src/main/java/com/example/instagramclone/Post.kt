package com.example.instagramclone

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.io.File

// description: String
// image: File
// user: User
@ParseClassName("Post")
class Post : ParseObject() {
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

    fun setDescription(description: String) {
        put(KEY_DESCRIPTION, description)
    }
    fun setImage(image: File) {
        put(KEY_IMAGE, ParseFile(image))
    }
    fun setUser(user: ParseUser) {
        put(KEY_USER, user)
    }

    companion object {
        val KEY_DESCRIPTION = "description"
        val KEY_IMAGE = "image"
        val KEY_USER = "user"
    }
}