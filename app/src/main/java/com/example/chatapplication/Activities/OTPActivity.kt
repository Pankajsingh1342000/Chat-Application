package com.example.chatapplication.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivityOtpactivityBinding
import com.example.chatapplication.utils.LoadingDialog
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class OTPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpactivityBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var verificationId : String
    private var loading = LoadingDialog(this,"Sending OTP...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpactivity)

        loading.startLoading()



        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val phoneNumber = intent.getStringExtra("phoneNumber")

        binding.phoneLbl.text = "Verify $phoneNumber"


        val options : PhoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(this@OTPActivity, "Error OTP", Toast.LENGTH_SHORT).show()
                    loading.dismiss()
                }

                override fun onCodeSent(verifyId: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verifyId, p1)
                    loading.dismiss()
                    verificationId = verifyId
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                    binding.otpView.requestFocus()
                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.otpView.setOtpCompletionListener { otp ->
            val credential = PhoneAuthProvider.getCredential(verificationId, otp!!)

            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        startActivity(Intent(this@OTPActivity, SetupProfileActivity::class.java))
                        finishAffinity()
                        Toast.makeText(this@OTPActivity, "Logged In", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@OTPActivity, "Failed", Toast.LENGTH_LONG).show()
                    }
                }
        }


    }
}