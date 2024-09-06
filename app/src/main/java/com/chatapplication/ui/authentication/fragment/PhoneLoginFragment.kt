package com.chatapplication.ui.authentication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.chatapplication.R
import com.chatapplication.databinding.FragmentPhoneLoginBinding

class PhoneLoginFragment : Fragment() {

    private lateinit var binding: FragmentPhoneLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnContinue.setOnClickListener{
            findNavController().navigate(R.id.action_phoneLoginFragment_to_verifyFragment)
        }
    }
}