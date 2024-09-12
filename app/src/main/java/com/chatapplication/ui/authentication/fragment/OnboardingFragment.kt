package com.chatapplication.ui.authentication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chatapplication.MainActivity
import com.chatapplication.R
import com.chatapplication.databinding.FragmentOnboardingBinding
import com.chatapplication.ui.authentication.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class OnboardingFragment : Fragment() {
    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentOnboardingBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            val userData = mapOf(
                "firstName" to binding.edtFirstName.text.toString(),
                "lastName" to binding.edtLastName.text.toString(),
                "phoneNumber" to FirebaseAuth.getInstance().currentUser?.phoneNumber.toString()
            )
            viewModel.saveNewUserToFirestore(userData)
            (activity as MainActivity).showMainContent()
            findNavController().popBackStack(R.id.phoneLoginFragment, true)
        }

    }
}