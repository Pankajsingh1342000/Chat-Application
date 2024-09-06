package com.chatapplication.ui.walkthrough

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chatapplication.R
import com.chatapplication.databinding.FragmentWalkthroughBinding

class WalkthroughFragment : Fragment() {

    private lateinit var binding: FragmentWalkthroughBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWalkthroughBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnWalkthrough.setOnClickListener{
            findNavController().navigate(R.id.action_walkthroughFragment_to_phoneLoginFragment)
        }
    }

}