package com.chatapplication.ui.authentication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatapplication.MainActivity
import com.chatapplication.databinding.FragmentVerifyBinding
import com.chatapplication.util.SharedPreferenceManager

class VerifyFragment : Fragment() {

    private lateinit var binding: FragmentVerifyBinding
    private lateinit var sharedPreference: SharedPreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyBinding.inflate(layoutInflater)
        sharedPreference = SharedPreferenceManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnContinue.setOnClickListener {
            onSuccessfulVerification()
        }
    }

    private fun onSuccessfulVerification() {
        sharedPreference.setAuthenticationStatus(true)
        (activity as? MainActivity)?.showMainContent()
    }
}