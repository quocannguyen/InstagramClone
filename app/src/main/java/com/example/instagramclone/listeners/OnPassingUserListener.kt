package com.example.instagramclone.listeners

import com.parse.ParseUser

interface OnPassingUserListener {
    fun onUserPassed(user: ParseUser)
}