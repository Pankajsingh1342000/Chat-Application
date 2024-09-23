package com.chatapplication.ui.feature.chat.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.chatapplication.databinding.FragmentChatsBinding
import com.chatapplication.ui.feature.chat.adapter.ChatsListAdapter
import com.chatapplication.ui.feature.chat.model.ChatList
import com.chatapplication.util.SharedPreferenceManager

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private lateinit var sharedPreference: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(layoutInflater)
        sharedPreference = SharedPreferenceManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create a list of 10 dummy chat data
        val dummyChats = List(10) { index ->
            ChatList(
                name = "Person $index",
                message = "This is a dummy message for person $index.",
                timeStamp = "10:0$index AM",
                unreadMessageCount = index
            )
        }

        // Set up the RecyclerView
        binding.rvChats.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ChatsListAdapter(dummyChats)


        }
    }
}