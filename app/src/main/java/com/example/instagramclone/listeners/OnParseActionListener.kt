package com.example.instagramclone.listeners

import com.parse.ParseException


interface OnParseActionListener {
    fun onParseSuccess()
    fun onParseException(parseException: ParseException)
}