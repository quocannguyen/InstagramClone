package com.example.instagramclone

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import java.io.File

@ParseClassName("Post")
class Post : ParseObject() {
    val id: String?
        get() {
            return getString(KEY_ID)
        }
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

    companion object {
        const val KEY_ID = "objectId"
        const val KEY_DESCRIPTION = "description"
        const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
    }
}