package com.chatapplication.ui.authentication.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.chatapplication.databinding.FragmentPhoneLoginBinding
import com.chatapplication.ui.authentication.viewmodel.AuthViewModel
import com.google.android.material.textfield.TextInputEditText
import com.hbb20.CountryCodePicker

class PhoneLoginFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentPhoneLoginBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var edtPhoneNumber: TextInputEditText
    private lateinit var btnContinue: Button
    private lateinit var countryCodePicker: CountryCodePicker
    private lateinit var progressBar: ProgressBar
    private lateinit var blurBackground: View
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhoneLoginBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        edtPhoneNumber = binding.edtPhoneNumber
        btnContinue = binding.btnContinue
        countryCodePicker = binding.countryCodePicker
        progressBar = binding.progressBar
        blurBackground = binding.blurBackground
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    private fun setListeners(){
        btnContinue.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnContinue -> {

                progressBar.visibility = View.VISIBLE
                blurBackground.visibility = View.VISIBLE

                val phoneNumber = countryCodePicker.textView_selectedCountry.text.toString().trim() +
                        edtPhoneNumber.text.toString().trim()

                if (edtPhoneNumber.text.toString().isNotEmpty()) {
                    viewModel.startPhoneNumberVerification(phoneNumber, requireActivity())

                    viewModel.verificationId.observe(viewLifecycleOwner) { verificationId ->
                        if (verificationId != null) {
                            val action = PhoneLoginFragmentDirections
                                .actionPhoneLoginFragmentToVerifyFragment(verificationId)
                            progressBar.visibility = View.GONE
                            blurBackground.visibility = View.GONE
                            navController.navigate(action)
                        } else {
                            Log.e("PhoneLoginFragment", "Verification ID is null")
                            progressBar.visibility = View.GONE
                            blurBackground.visibility = View.GONE
                        }
                    }
                } else {
                    progressBar.visibility = View.GONE
                    blurBackground.visibility = View.GONE
                    Toast.makeText(requireContext(), "Enter valid phone number", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}