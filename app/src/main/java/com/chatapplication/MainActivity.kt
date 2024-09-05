package com.chatapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.chatapplication.ui.feature.call.fragment.CallsFragment
import com.chatapplication.ui.feature.chat.fragment.ChatsFragment
import com.chatapplication.ui.feature.update.fragment.UpdatesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)

        viewPager.adapter = object : FragmentStateAdapter(this) {

            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment {

                return when (position) {
                    0 -> ChatsFragment()   // Chats
                    1 -> UpdatesFragment() // Updates
                    2 -> CallsFragment()   // Calls
                    else -> ChatsFragment()
                }
            }
        }

        // Sync ViewPager2 swipes with BottomNavigationView clicks
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.chatsFragment -> viewPager.setCurrentItem(0, true)
                R.id.updatesFragment -> viewPager.setCurrentItem(1, true)
                R.id.callsFragment -> viewPager.setCurrentItem(2, true)
            }
            true
        }

        // Sync ViewPager2 swipe with BottomNavigationView item selection
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> bottomNavigationView.selectedItemId = R.id.chatsFragment
                    1 -> bottomNavigationView.selectedItemId = R.id.updatesFragment
                    2 -> bottomNavigationView.selectedItemId = R.id.callsFragment
                }
            }
        })


    }

}