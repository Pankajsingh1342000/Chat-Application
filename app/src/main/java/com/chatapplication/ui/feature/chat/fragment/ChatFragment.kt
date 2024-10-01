package com.chatapplication.ui.feature.chat.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chatapplication.databinding.FragmentChatBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var btnBack: ImageView
    private lateinit var profileImage: ShapeableImageView
    private lateinit var name: TextView
    private lateinit var videoCall: ImageView
    private lateinit var call: ImageView
    private lateinit var menu : ImageView
    private lateinit var rvMessage: RecyclerView
    private lateinit var ibEmoji: ImageButton
    private lateinit var ibAttach: ImageButton
    private lateinit var ibCamera: ImageButton
    private lateinit var fabMic: FloatingActionButton
    private lateinit var fabSend: FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }
}