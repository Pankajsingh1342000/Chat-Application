package com.chatapplication.ui.feature.update.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatapplication.databinding.FragmentHostUpdatesBinding

class UpdatesHostFragment : Fragment() {
    private lateinit var binding: FragmentHostUpdatesBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHostUpdatesBinding.inflate(layoutInflater)
        return binding.root
    }
}