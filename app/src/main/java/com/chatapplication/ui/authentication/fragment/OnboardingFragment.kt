package com.chatapplication.ui.authentication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.chatapplication.MainActivity
import com.chatapplication.R
import com.chatapplication.databinding.FragmentOnboardingBinding
import com.chatapplication.ui.authentication.viewmodel.AuthViewModel
import com.chatapplication.util.SharedPreferenceManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class OnboardingFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var sharedPreference: SharedPreferenceManager
    private lateinit var viewModel: AuthViewModel
    private lateinit var edtFirstName: TextInputEditText
    private lateinit var edtLastName: TextInputEditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: ImageView
    private lateinit var navController: NavController
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnboardingBinding.inflate(layoutInflater)
        sharedPreference = SharedPreferenceManager(requireContext())
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        edtFirstName = binding.edtFirstName
        edtLastName = binding.edtLastName
        btnSave = binding.btnSave
        btnBack = binding.ivBackIcon
        navController = findNavController()
        firebaseAuth = FirebaseAuth.getInstance()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            navController.navigate(R.id.action_onboardingFragment_to_walkthroughFragment)
        }
    }

    private fun setListeners() {
        btnSave.setOnClickListener(this)
        btnBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnSave -> {
                val userData = mapOf(
                    "firstName" to edtFirstName.text.toString(),
                    "lastName" to edtLastName.text.toString(),
                    "phoneNumber" to firebaseAuth.currentUser?.phoneNumber.toString()
                )
                viewModel.saveNewUserToFirestore(userData)
                sharedPreference.setAuthenticationStatus(true)
                (activity as MainActivity).showMainContent()
                navController.popBackStack(R.id.phoneLoginFragment, true)
            }
            btnBack -> {
                navController.navigate(R.id.action_onboardingFragment_to_walkthroughFragment)
            }
        }
    }
}