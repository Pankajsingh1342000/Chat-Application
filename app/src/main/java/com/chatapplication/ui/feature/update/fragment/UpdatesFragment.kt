package com.chatapplication.ui.feature.update.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chatapplication.MainActivity
import com.chatapplication.databinding.FragmentUpdatesBinding
import com.chatapplication.util.SharedPreferenceManager

class UpdatesFragment : Fragment() {

    private lateinit var binding: FragmentUpdatesBinding
    private lateinit var sharedPreference: SharedPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdatesBinding.inflate(layoutInflater)
        sharedPreference = SharedPreferenceManager(requireContext())
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdateLogout.setOnClickListener{
            sharedPreference.logout()
            (activity as? MainActivity)?.showAuthContent()
        }
    }

}