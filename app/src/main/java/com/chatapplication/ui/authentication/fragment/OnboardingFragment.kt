package com.chatapplication.ui.authentication.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.activity.addCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.chatapplication.MainActivity
import com.chatapplication.R
import com.chatapplication.databinding.FragmentOnboardingBinding
import com.chatapplication.ui.authentication.viewmodel.AuthViewModel
import com.chatapplication.util.SharedPreferenceManager
import com.google.android.material.imageview.ShapeableImageView
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
    private lateinit var ivProfileImage: ShapeableImageView
    private lateinit var navController: NavController
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null

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
        ivProfileImage = binding.ivProfileImage
        progressBar = binding.progressBar
        navController = findNavController()
        firebaseAuth = FirebaseAuth.getInstance()
        imagePickerLauncher = this.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                selectedImageUri = result.data?.data
                binding.ivProfileImage.setImageURI(selectedImageUri)  // Display selected image
            }
        }
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
        ivProfileImage.setOnClickListener(this)
        btnBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            btnSave -> {
                progressBar.visibility = View.VISIBLE
                if (selectedImageUri != null) {
                    uploadProfileImageAndSaveUserData(selectedImageUri!!)
                } else {
                    progressBar.visibility = View.GONE
                    saveUserData(null)  // Skip image upload
                }
            }
            ivProfileImage -> {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                imagePickerLauncher.launch(intent)
            }
            btnBack -> {
                navController.navigate(R.id.action_onboardingFragment_to_walkthroughFragment)
            }
        }
    }

    private fun uploadProfileImageAndSaveUserData(imageUri: Uri) {
        // Upload the selected image to Firebase Storage and save the user data
        viewModel.uploadImageToFirebase(imageUri, { imageUrl ->
            saveUserData(imageUrl)  // Once image is uploaded, save the user data with the image URL
        }, {
            // Handle failure in image upload
        })
    }
    private fun saveUserData(imageUrl: String?) {
        // Save user data to Firestore with or without image
        val userData = mapOf(
            "firstName" to edtFirstName.text.toString(),
            "lastName" to edtLastName.text.toString(),
            "phoneNumber" to firebaseAuth.currentUser?.phoneNumber.toString(),
            "profileImage" to imageUrl  // Image URL or null if skipped
        )
        viewModel.saveNewUserToFirestore(userData)
        sharedPreference.setAuthenticationStatus(true)
        progressBar.visibility = View.GONE
        (activity as MainActivity).showMainContent()
        navController.popBackStack(R.id.phoneLoginFragment, true)
    }
}