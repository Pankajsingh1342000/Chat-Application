package com.chatapplication.ui.feature.call.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatapplication.MainActivity
import com.chatapplication.databinding.FragmentCallsBinding
import com.chatapplication.util.SharedPreferenceManager

class CallsFragment : Fragment() {

    private lateinit var binding: FragmentCallsBinding
    private lateinit var sharedPreference: SharedPreferenceManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCallsBinding.inflate(layoutInflater)
        sharedPreference = SharedPreferenceManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreference = SharedPreferenceManager(requireContext())

        binding.btnCallLogout.setOnClickListener{
            sharedPreference.logout()
            (activity as? MainActivity)?.showAuthContent()
        }
    }

}