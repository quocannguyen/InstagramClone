package com.example.instagramclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.parse.*
import com.example.instagramclone.fragments.ComposeFragment
import com.example.instagramclone.fragments.FeedFragment
import com.example.instagramclone.fragments.PostDetailFragment
import com.example.instagramclone.listeners.OnPassingPostListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


/**
 * Let user create a post by taking a photo with their camera
 */
const val PARCELABLE_KEY_POST = "post"
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setBottomNavigation()
        TwitterCloneApplication.liveQueries()
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

    private fun setBottomNavigation() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener(object: NavigationBarView.OnItemSelectedListener {
            lateinit var fragmentToShow: Fragment

            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.action_home -> {
                        val feedFragment = FeedFragment.newInstance()
                        feedFragment.onPassingPostListener = object: OnPassingPostListener {
                            override fun onPostPassed(post: Post) {
                                val bundle = Bundle()
                                bundle.putParcelable(PARCELABLE_KEY_POST, post)
                                val postDetailFragment = PostDetailFragment.newInstance(bundle)
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.flContainer, postDetailFragment)
                                    .commit()
                            }
                        }
                        fragmentToShow = feedFragment
                    }
                    R.id.action_compose -> {
                        fragmentToShow = ComposeFragment.newInstance()
                    }
                    R.id.action_profile -> {
//                        fragmentToShow = ProfileFragment.newInstance(object: OnFragmentCallListener {
//                            override fun onFragmentCall(post: Post) {
////                                Log.d("peter", "MainActivity onFragmentCall: $string")
////                                supportFragmentManager.beginTransaction()
////                                    .replace(R.id.flContainer, PostDetailFragment.newInstance())
////                                    .commit()
//                            }
//                        })
                    }
                }

                supportFragmentManager.beginTransaction().replace(R.id.flContainer, fragmentToShow).commit()
                return true
            }
        })
        bottomNavigationView.selectedItemId = R.id.action_home
    }
}