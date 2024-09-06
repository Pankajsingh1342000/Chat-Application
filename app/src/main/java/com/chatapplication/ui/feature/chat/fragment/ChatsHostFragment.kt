package com.chatapplication.ui.feature.chat.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatapplication.databinding.FragmentHostChatsBinding

class ChatsHostFragment : Fragment() {
    private lateinit var binding: FragmentHostChatsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHostChatsBinding.inflate(layoutInflater)
        return binding.root
    }
}