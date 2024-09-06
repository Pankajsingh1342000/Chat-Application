package com.chatapplication.ui.feature.call.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatapplication.databinding.FragmentHostCallsBinding

class CallsHostFragment : Fragment() {
    private lateinit var binding: FragmentHostCallsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHostCallsBinding.inflate(layoutInflater)
        return binding.root
    }

}