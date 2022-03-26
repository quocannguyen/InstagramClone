package com.example.instagramclone

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.core.content.FileProvider
import com.parse.*
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import java.io.File
import java.net.URI

/**
 * Let user create a post by taking a photo with their camera
 */
class MainActivity : AppCompatActivity() {

    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    private val photoFileName = "instagram_clone_photo.jpg"
    private var photoFile: File? = null

    lateinit var etDescription: EditText
    lateinit var ivPhoto: ImageView
    lateinit var pbLoading: ProgressBar

    lateinit var subscriptionHandling: SubscriptionHandling<Post>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etDescription = findViewById(R.id.etDescription)
        ivPhoto = findViewById(R.id.ivPhoto)
        pbLoading = findViewById(R.id.pbLoading)

        setButtons()
        liveQueries()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                val resizedBitmap = Bitmap.createScaledBitmap(takenImage, 150, 100, true)
                Log.d("peter", "MainActivity onActivityResult: ${resizedBitmap.byteCount}")
                // Load the taken image into a preview
                ivPhoto.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
//        return super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.miSignOut) {
            ParseUser.logOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setButtons() {
        findViewById<Button>(R.id.btnPhotograph).setOnClickListener {
            // Launch camera to let user take picture
            launchCamera()
        }
        findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            // Submit post to server
            val description = etDescription.text
            val user = ParseUser.getCurrentUser()
            if (photoFile != null) {
                pbLoading.visibility = ProgressBar.VISIBLE
                TwitterCloneApplication.submitPost(this@MainActivity, description.toString(), user, photoFile!!)
                onPostSubmitted()
            } else {
                Log.e("peter", "MainActivity setButtons: photoFile == null", )
                Toast.makeText(this, "Missing photo", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Query for all posts on server
    private fun queryPosts() {
        // Specify which class to query
        val query = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        // Find all Post objects
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e != null) {
                    Log.e("peter", "MainActivity queryPosts done: $e", )
                    Toast.makeText(this@MainActivity, "Error fetching posts", Toast.LENGTH_LONG).show()
                } else {
                    if (posts != null) {
                        for (post in posts) {
                            Log.i("peter", "MainActivity queryPosts done: $post")
                        }
                    }
                }
            }
        })
    }

    private fun launchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)
        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    private fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Twitter Clone peter")
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d("peter", "failed to create directory")
        }
        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    private fun onPostSubmitted() {
        etDescription.text = null
        ivPhoto.setImageBitmap(null)
        pbLoading.visibility = ProgressBar.INVISIBLE
        Toast.makeText(this, "Post submitted", Toast.LENGTH_LONG).show()
    }

    fun liveQueries() {
        Log.d("peter", "MainActivity liveQueries: ")
        val webSocketUrl = "wss://codepathinstagram.b4a.io/"
        val webSocketUri = URI(webSocketUrl)
        val parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(webSocketUri)

//        val parseQuery = ParseQuery<ParseObject>("Post")
        val parseQuery = ParseQuery.getQuery(Post::class.java)
        subscriptionHandling = parseLiveQueryClient!!.subscribe(parseQuery)
        subscriptionHandling.handleSubscribe {
            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE
            ) { _: ParseQuery<Post?>?, `object`: ParseObject? ->
    //            runOnUiThread { messagesAdapter!!.addItem(`object`) }
                Log.d("peter", "MainActivity liveQueries CREATE: $`object`")
            }
            subscriptionHandling.handleEvent(SubscriptionHandling.Event.DELETE
            ) { _: ParseQuery<Post?>?, parseObject: ParseObject? ->
//                runOnUiThread { messagesAdapter!!.removeItem(`object`!!) }
                Log.d("peter", "MainActivity liveQueries DELETE: $parseObject")
            }
            subscriptionHandling.handleEvent(SubscriptionHandling.Event.UPDATE
            ) { _: ParseQuery<Post?>?, `object`: ParseObject? ->
//                runOnUiThread { messagesAdapter!!.updateItem(`object`!!) }
                Log.d("peter", "MainActivity liveQueries UPDATE: $`object`")
            }
        }
    }
}