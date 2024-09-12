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

    private var _binding: FragmentWalkthroughBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWalkthroughBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigate to PhoneLoginFragment
        binding.btnWalkthrough.setOnClickListener {
            findNavController().navigate(R.id.action_walkthroughFragment_to_phoneLoginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}