package com.example.instagramclone

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Environment
import android.util.Log
import com.parse.*
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.math.BigInteger
import java.net.URI
import java.security.MessageDigest


class TwitterCloneApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ParseObject.registerSubclass(Post::class.java)
        ParseObject.registerSubclass(Comment::class.java)
        ParseObject.registerSubclass(UserPostInteraction::class.java)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build())
    }

    companion object {

        // Returns the File for a photo stored on disk given the fileName
        fun getPhotoFileUri(context: Context, fileName: String): File {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            val mediaStorageDir =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Twitter Clone peter")
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
                Log.d("peter", "failed to create directory")
            }
            // Return the file target for the photo based on filename
            return File(mediaStorageDir.path + File.separator + fileName)
        }

        fun rotateBitmapOrientation(photoFilePath: String?): Bitmap? {
            // Create and configure BitmapFactory
            val bounds = BitmapFactory.Options()
            bounds.inJustDecodeBounds = true
            BitmapFactory.decodeFile(photoFilePath, bounds)
            val opts = BitmapFactory.Options()
            val bm = BitmapFactory.decodeFile(photoFilePath, opts)
            // Read EXIF Data
            var exif: ExifInterface? = null
            try {
                exif = ExifInterface(photoFilePath!!)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val orientString = exif!!.getAttribute(ExifInterface.TAG_ORIENTATION)
            val orientation = orientString?.toInt() ?: ExifInterface.ORIENTATION_NORMAL
            var rotationAngle = 0
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270
            // Rotate Bitmap
            val matrix = Matrix()
            matrix.setRotate(rotationAngle.toFloat(), bm.width.toFloat() / 2, bm.height.toFloat() / 2)
            // Return result
            return Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true)
        }

        fun liveQueries() {
            Log.d("peter", "MainActivity liveQueries: ")
            val webSocketUrl = "wss://codepathinstagram.b4a.io/"
            val webSocketUri = URI(webSocketUrl)
            val parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(webSocketUri)

//        val parseQuery = ParseQuery<ParseObject>("Post")
            val parseQuery = ParseQuery.getQuery(Post::class.java)
            val subscriptionHandling = parseLiveQueryClient!!.subscribe(parseQuery)
            subscriptionHandling.handleSubscribe {
                subscriptionHandling.handleEvent(
                    SubscriptionHandling.Event.CREATE
                ) { _: ParseQuery<Post?>?, `object`: ParseObject? ->
                    //            runOnUiThread { messagesAdapter!!.addItem(`object`) }
                    Log.d("peter", "MainActivity liveQueries CREATE: $`object`")
                }
                subscriptionHandling.handleEvent(
                    SubscriptionHandling.Event.DELETE
                ) { _: ParseQuery<Post?>?, parseObject: ParseObject? ->
//                runOnUiThread { messagesAdapter!!.removeItem(`object`!!) }
                    Log.d("peter", "MainActivity liveQueries DELETE: $parseObject")
                }
                subscriptionHandling.handleEvent(
                    SubscriptionHandling.Event.UPDATE
                ) { _: ParseQuery<Post?>?, `object`: ParseObject? ->
//                runOnUiThread { messagesAdapter!!.updateItem(`object`!!) }
                    Log.d("peter", "MainActivity liveQueries UPDATE: $`object`")
                }
            }
        }

        private fun getGravatarUrl(userId: String?): String {
            var hex = ""
            try {
                val digest = MessageDigest.getInstance("MD5")
                val hash = digest.digest(userId?.toByteArray())
                val bigInt = BigInteger(hash)
                hex = bigInt.abs().toString(16)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return "https://www.gravatar.com/avatar/$hex?d=identicon"
        }

        fun getProfileImageUrl(user: ParseUser?): String {
            val profileImage = user?.getParseFile("profilePhoto")
            val profileImageUrl = when (profileImage) {
                null -> getGravatarUrl(user?.objectId)
                else -> profileImage.url
            }
            return profileImageUrl
        }
    }
}