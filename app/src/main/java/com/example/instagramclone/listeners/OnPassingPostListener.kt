package com.example.instagramclone.listeners

import com.example.instagramclone.Post

interface OnPassingPostListener {
    fun onPostPassed(post: Post)
}