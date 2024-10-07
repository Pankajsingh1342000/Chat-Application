package com.chatapplication

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chatapplication.ui.feature.call.fragment.CallsHostFragment
import com.chatapplication.ui.feature.chat.fragment.ChatsHostFragment
import com.chatapplication.ui.feature.update.fragment.UpdatesHostFragment
import com.chatapplication.util.FabClickListener
import com.chatapplication.util.SharedPreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewPager: ViewPager2
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var authNavHostFragment: FragmentContainerView
    private lateinit var sharedPreferences: SharedPreferenceManager
    private var fabClickListener: FabClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)
        initViews()

        sharedPreferences = SharedPreferenceManager(this)

        floatingActionButton.setOnClickListener {
            fabClickListener?.onFabClick()
        }

        if(sharedPreferences.isAuthenticated()){
            showMainContent()
        }
        else{
            showAuthContent()
        }

    }

    fun showAuthContent() {
        viewPager.visibility = View.GONE
        bottomNavigationView.visibility = View.GONE
        floatingActionButton.visibility = View.GONE
        authNavHostFragment.visibility = View.VISIBLE
        setUpMainContent()

    }

    fun showMainContent() {
        authNavHostFragment.visibility = View.GONE
        viewPager.visibility = View.VISIBLE
        bottomNavigationView.visibility = View.VISIBLE
        floatingActionButton.visibility = View.VISIBLE
        setUpMainContent()
    }

    fun hideMainContent() {
        authNavHostFragment.visibility = View.GONE
        bottomNavigationView.visibility = View.GONE
        floatingActionButton.visibility = View.GONE
    }
    fun setViewPagerSwipeEnabled(isEnabled: Boolean) {
        viewPager.isUserInputEnabled = isEnabled
    }

    fun setFabClickListener(listener: FabClickListener?) {
        fabClickListener = listener
    }


    private fun setUpMainContent() {
        setupWindowInsets()
        setupViewPager()
        setupBottomNavigation()
    }

    private fun initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        viewPager = findViewById(R.id.view_pager)
        authNavHostFragment = findViewById(R.id.auth_nav_host_fragment)
        floatingActionButton = findViewById(R.id.fab_add)
    }

    private fun setupWindowInsets() {
        val mainLayout = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Apply padding for status and navigation bars
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            bottomNavigationView.setPadding(0, 0, 0, systemBars.bottom)
            authNavHostFragment.setPadding(0, 0, 0, systemBars.bottom)

            insets
        }
    }

    private fun setupViewPager() {
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 3

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> ChatsHostFragment()
                    1 -> UpdatesHostFragment()
                    2 -> CallsHostFragment()
                    else -> ChatsHostFragment()
                }
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottomNavigationView.selectedItemId = when (position) {
                    0 -> R.id.chatListFragment
                    1 -> R.id.updatesFragment
                    2 -> R.id.callsFragment
                    else -> R.id.chatListFragment
                }

                when (position) {
                    0 -> floatingActionButton.apply{
                        visibility = View.VISIBLE
                        setImageResource(R.drawable.ic_fab_chat_icon)
                    }
                    1 -> floatingActionButton.apply{
                        visibility = View.VISIBLE
                        setImageResource(R.drawable.ic_fab_camera_icon)
                    }
                    2 -> floatingActionButton.apply{
                        visibility = View.GONE
                    }
                    else -> floatingActionButton.visibility = View.GONE
                }

                val fragment = supportFragmentManager.findFragmentByTag("f${viewPager.currentItem}")
                (fragment as? FabClickListener)?.let {
                    setFabClickListener(it)
                }
            }
        })
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.chatListFragment -> viewPager.setCurrentItem(0, true)
                R.id.updatesFragment -> viewPager.setCurrentItem(1, true)
                R.id.callsFragment -> viewPager.setCurrentItem(2, true)
            }
            true
        }
    }
}