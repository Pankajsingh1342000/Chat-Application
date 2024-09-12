package com.chatapplication.ui.authentication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chatapplication.R
import com.chatapplication.databinding.FragmentPhoneLoginBinding
import com.chatapplication.ui.authentication.viewmodel.AuthViewModel
import com.google.firebase.auth.PhoneAuthProvider

class PhoneLoginFragment : Fragment() {

    private lateinit var binding: FragmentPhoneLoginBinding
    private lateinit var viewModel: AuthViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnContinue.setOnClickListener {
            val phoneNumber = binding.countryCodePicker.textView_selectedCountry.text.toString().trim() +
                    binding.edtPhoneNumber.text.toString().trim()
            if (phoneNumber.isNotEmpty()) {
                viewModel.startPhoneNumberVerification(phoneNumber, requireActivity())
                viewModel.verificationId.observe(viewLifecycleOwner) { verificationId ->
                    if (verificationId != null) {
                        val action = PhoneLoginFragmentDirections
                            .actionPhoneLoginFragmentToVerifyFragment(verificationId)
                        findNavController().navigate(action)
                    } else {
                        Log.e("PhoneLoginFragment", "Verification ID is null")
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Enter valid phone number", Toast.LENGTH_SHORT).show()
            }
        }
    }
}