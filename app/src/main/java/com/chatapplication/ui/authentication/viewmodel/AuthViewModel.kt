package com.chatapplication.ui.authentication.viewmodel

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private val _verificationId = MutableLiveData<String>()
    val verificationId: LiveData<String> = _verificationId


    // LiveData for verification status
    private val _verificationStatus = MutableLiveData<Boolean>()
    val verificationStatus: LiveData<Boolean> = _verificationStatus

    // LiveData for user existence in Firestore
    private val _isExistingUser = MutableLiveData<Boolean>()
    val isExistingUser: LiveData<Boolean> = _isExistingUser

    // Function to start phone number verification
    fun startPhoneNumberVerification(phoneNumber: String, activity: Activity) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-verification or Instant verification
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("AuthViewModel", "Verification failed: ${e.message}")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
//                    this@AuthViewModel.verificationId = verificationId
                    _verificationId.postValue(verificationId)
                    this@AuthViewModel.resendToken = token
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // Function to verify OTP
    fun verifyOtp(verificationId: String, otp: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        signInWithPhoneAuthCredential(credential)
    }

    // Function to sign in with phone credential
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _verificationStatus.postValue(true)
                checkIfUserExistsInFirestore()
            } else {
                _verificationStatus.postValue(false)
                Log.e("AuthViewModel", "Error signing in: ${task.exception?.message}")
            }
        }
    }

    // Check if user already exists in Firestore
    private fun checkIfUserExistsInFirestore() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    _isExistingUser.postValue(document.exists())  // LiveData posting is asynchronous
                } else {
                    Log.d("AuthViewModel", "User Does Not Exist")
                    _isExistingUser.postValue(false)
                }

            }
            .addOnFailureListener { e ->
                Log.e("AuthViewModel", "Error checking user existence: ${e.message}")
            }
    }

    // Save new user to Firestore if they don't exist
    fun saveNewUserToFirestore(userData: Map<String, Any>) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("users").document(userId).set(userData)
            .addOnSuccessListener {
                Log.d("AuthViewModel", "New user added to Firestore")
            }
            .addOnFailureListener { e ->
                Log.e("AuthViewModel", "Error adding user to Firestore: ${e.message}")
            }
    }
}
