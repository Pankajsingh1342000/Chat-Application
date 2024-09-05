package com.chatapplication.ui.home.feature.update.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatapplication.R
import com.chatapplication.databinding.FragmentUpdatesBinding

class UpdatesFragment : Fragment() {
    private lateinit var binding: FragmentUpdatesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdatesBinding.inflate(layoutInflater)
        return binding.root
    }
}