package com.example.instagramclone.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.instagramclone.Post
import com.example.instagramclone.R
import com.example.instagramclone.TwitterCloneApplication
import com.parse.ParseException
import com.parse.ParseUser
import com.parse.SaveCallback
import java.io.File

class ComposeFragment : Fragment() {

    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    private val photoFileName = "instagram_clone_photo.jpg"
    private var photoFile: File? = null

    lateinit var etDescription: EditText
    lateinit var ivPhoto: ImageView
    lateinit var pbLoading: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etDescription = view.findViewById(R.id.etDescription)
        ivPhoto = view.findViewById(R.id.ivPhoto)
        pbLoading = view.findViewById(R.id.pbLoading)
        // Set onClickListeners and setup logic
        setButtons(view)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                // RESIZE BITMAP, see section below
//                val resizedBitmap = Bitmap.createScaledBitmap(takenImage, 150, 100, true)
//                Log.d("peter", "MainActivity onActivityResult: ${resizedBitmap.byteCount}")
                val rotatedBitmap = TwitterCloneApplication.rotateBitmapOrientation(photoFile!!.absolutePath)
                // Load the taken image into a preview
                ivPhoto.setImageBitmap(rotatedBitmap)
            } else { // Result was a failure
                Toast.makeText(requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setButtons(view: View) {
        view.findViewById<Button>(R.id.btnPhotograph).setOnClickListener {
            // Launch camera to let user take picture
            launchCamera()
        }
        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            // Submit post to server
            val description = etDescription.text
            val user = ParseUser.getCurrentUser()
            when {
                description.isNullOrEmpty() -> {
                    Log.e("peter", "ComposeFragment setButtons: description.isNullOrEmpty()")
                    Toast.makeText(requireContext(), "Description is required", Toast.LENGTH_LONG).show()
                }
                photoFile == null -> {
                    Log.e("peter", "ComposeFragment setButtons: photoFile == null")
                    Toast.makeText(requireContext(), "Missing photo", Toast.LENGTH_LONG).show()
                }
                else -> {
                    pbLoading.visibility = ProgressBar.VISIBLE
                    submitPost(requireContext(), description.toString(), user, photoFile!!)
                }
            }
        }
    }

    private fun launchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = TwitterCloneApplication.getPhotoFileUri(requireContext(), photoFileName)
        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider =
                FileProvider.getUriForFile(requireContext(), getString(R.string.file_provider_authority), photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(requireContext().packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }

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
                    onPostSubmitted()
                } else {
                    Toast.makeText(context, "Error submitting post", Toast.LENGTH_LONG).show()
                    Log.e("peter", "TwitterCloneApplication submitPost done: $e", )
                }
            }
        })
    }

    private fun onPostSubmitted() {
        etDescription.text = null
        photoFile = null
        ivPhoto.setImageBitmap(null)
        pbLoading.visibility = ProgressBar.INVISIBLE
        Toast.makeText(requireContext(), "Post submitted", Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance(): ComposeFragment {
            return ComposeFragment()
        }
    }
}