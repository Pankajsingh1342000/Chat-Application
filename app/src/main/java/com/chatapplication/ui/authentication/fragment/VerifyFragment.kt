package com.chatapplication.ui.authentication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chatapplication.MainActivity
import com.chatapplication.R
import com.chatapplication.databinding.FragmentVerifyBinding
import com.chatapplication.ui.authentication.viewmodel.AuthViewModel
import com.chatapplication.util.SharedPreferenceManager

class VerifyFragment : Fragment() {

    private lateinit var binding: FragmentVerifyBinding
    private lateinit var sharedPreference: SharedPreferenceManager
    private lateinit var viewModel: AuthViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVerifyBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        sharedPreference = SharedPreferenceManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = VerifyFragmentArgs.fromBundle(requireArguments())
        val verificationId = args.verificationId

        binding.btnContinue.setOnClickListener {
            val otpCode = binding.etOtpDigit1.text.toString().trim() +
                    binding.etOtpDigit2.text.toString().trim() +
                    binding.etOtpDigit3.text.toString().trim() +
                    binding.etOtpDigit4.text.toString().trim() +
                    binding.etOtpDigit5.text.toString().trim() +
                    binding.etOtpDigit6.text.toString().trim()

            if (otpCode.isNotEmpty() && verificationId.isNotEmpty()) {
                viewModel.verifyOtp(verificationId, otpCode)
            } else {
                Toast.makeText(requireContext(), "Enter valid OTP", Toast.LENGTH_SHORT).show()
            }
        }
        observeViewModel()
    }


    private fun observeViewModel() {

        viewModel.isExistingUser.observe(viewLifecycleOwner, Observer {
            if (it) {
                sharedPreference.setAuthenticationStatus(true)
                findNavController().popBackStack(R.id.phoneLoginFragment, true)
                (activity as? MainActivity)?.showMainContent()
            }else{
                findNavController().navigate(R.id.action_verifyFragment_to_onboardingFragment)
            }
        })

    }
}