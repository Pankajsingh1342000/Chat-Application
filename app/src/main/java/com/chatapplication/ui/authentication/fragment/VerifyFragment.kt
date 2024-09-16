    package com.chatapplication.ui.authentication.fragment

    import android.os.Bundle
    import android.text.Editable
    import android.text.TextWatcher
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
    import com.chatapplication.MainActivity
    import com.chatapplication.R
    import com.chatapplication.databinding.FragmentVerifyBinding
    import com.chatapplication.ui.authentication.viewmodel.AuthViewModel
    import com.chatapplication.util.SharedPreferenceManager
    import com.chatapplication.util.Util
    import com.google.android.material.textfield.TextInputEditText

    class VerifyFragment : Fragment(), View.OnClickListener {

        private lateinit var binding: FragmentVerifyBinding
        private lateinit var sharedPreference: SharedPreferenceManager
        private lateinit var viewModel: AuthViewModel
        private lateinit var edtOtpDigit1: TextInputEditText
        private lateinit var edtOtpDigit2: TextInputEditText
        private lateinit var edtOtpDigit3: TextInputEditText
        private lateinit var edtOtpDigit4: TextInputEditText
        private lateinit var edtOtpDigit5: TextInputEditText
        private lateinit var edtOtpDigit6: TextInputEditText
        private lateinit var btnContinue: Button
        private lateinit var progressBar: ProgressBar
        private lateinit var blurBackground: View
        private lateinit var navController: NavController
        private lateinit var verificationId: String
        private lateinit var otpFields: List<TextInputEditText>

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentVerifyBinding.inflate(layoutInflater)
            viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
            sharedPreference = SharedPreferenceManager(requireContext())
            edtOtpDigit1 = binding.etOtpDigit1
            edtOtpDigit2 = binding.etOtpDigit2
            edtOtpDigit3 = binding.etOtpDigit3
            edtOtpDigit4 = binding.etOtpDigit4
            edtOtpDigit5 = binding.etOtpDigit5
            edtOtpDigit6 = binding.etOtpDigit6
            btnContinue = binding.btnContinue
            progressBar = binding.progressBar
            blurBackground = binding.blurBackground
            navController = findNavController()
            val args = VerifyFragmentArgs.fromBundle(requireArguments())
            verificationId = args.verificationId

            otpFields = listOf(
                edtOtpDigit1,
                edtOtpDigit2,
                edtOtpDigit3,
                edtOtpDigit4,
                edtOtpDigit5,
                edtOtpDigit6
            )

            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            setListeners()
            existingUserOrNot()
            setOtpFieldListeners()
        }
        private fun setListeners(){
            btnContinue.setOnClickListener(this)
        }

        private fun existingUserOrNot() {

            viewModel.isExistingUser.observe(viewLifecycleOwner) {
                if (it) {
                    blurBackground.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    sharedPreference.setAuthenticationStatus(true)
                    navController.popBackStack(R.id.phoneLoginFragment, true)
                    (activity as? MainActivity)?.showMainContent()
                } else {
                    blurBackground.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    navController.navigate(R.id.action_verifyFragment_to_onboardingFragment)
                }
            }

        }

        private fun setOtpFieldListeners() {
            otpFields.forEachIndexed { index, editText ->
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        if (!s.isNullOrEmpty()) {
                            // Move to the next field on input, if not the last field
                            if (index < otpFields.size - 1) {
                                otpFields[index + 1].requestFocus()
                            }
                        }
                    }

                    override fun afterTextChanged(s: Editable?) {
                        if (s != null && s.length == 1) {
                            // When user enters a digit, move focus to the next field if not the last one
                            if (index < otpFields.size - 1) {
                                otpFields[index + 1].requestFocus()
                            }
                        } else if (s.isNullOrEmpty()) {
                            // When user clears a field, keep the focus on the current field for re-entry
                            editText.requestFocus()
                        }
                    }
                })

                // Handle backspace key to clear the current field and focus on the previous one
                editText.setOnKeyListener { _, keyCode, event ->
                    if (event.action == android.view.KeyEvent.ACTION_DOWN &&
                        keyCode == android.view.KeyEvent.KEYCODE_DEL) {
                        if (editText.text.isNullOrEmpty() && index > 0) {
                            otpFields[index - 1].requestFocus()  // Move to previous field when backspace
                            otpFields[index - 1].text = null  // Clear previous field on backspace
                        } else {
                            editText.text = null  // Clear current field on backspace
                        }
                        true
                    } else {
                        false
                    }
                }

                // Ensure that only one cursor is visible at a time
                editText.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        editText.setSelection(editText.text?.length ?: 0)  // Place cursor at the end
                    }
                }
            }
        }
        override fun onClick(v: View?) {
            when (v) {
                btnContinue -> {
                    blurBackground.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.VISIBLE
                    Util.KeyboardHelper.hideKeyboard(requireActivity())
                    val otpCode = edtOtpDigit1.text.toString().trim() +
                            edtOtpDigit2.text.toString().trim() +
                            edtOtpDigit3.text.toString().trim() +
                            edtOtpDigit4.text.toString().trim() +
                            edtOtpDigit5.text.toString().trim() +
                            edtOtpDigit6.text.toString().trim()

                    if (otpCode.isNotEmpty() && verificationId.isNotEmpty()) {
                        blurBackground.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        viewModel.verifyOtp(verificationId, otpCode)
                    } else {
                        blurBackground.visibility = View.GONE
                        progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), "Enter valid OTP", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }