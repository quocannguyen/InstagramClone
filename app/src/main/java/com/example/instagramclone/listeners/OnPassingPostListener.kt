package com.example.instagramclone.listeners

import androidx.fragment.app.Fragment
import com.example.instagramclone.Post

interface OnPassingPostListener {
    fun onPostPassed(post: Post)
}