package com.example.instagramclone

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.parse.*
import java.io.File

class TwitterCloneApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ParseObject.registerSubclass(Post::class.java)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build())
    }

    companion object {

        // Send a Post object to Parse server
        fun submitPost(context: Context, description: String, user: ParseUser, photoFile: File) {
            val post = Post()
            post.setDescription(description)
            post.setUser(user)
            post.setImage(photoFile)
            post.saveInBackground(object: SaveCallback {
                override fun done(e: ParseException?) {
                    if (e == null) {
                        Toast.makeText(context, "Post submitted", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Error submitting post", Toast.LENGTH_LONG).show()
                        Log.e("peter", "TwitterCloneApplication submitPost done: $e", )
                    }
                }
            })
        }
    }
}