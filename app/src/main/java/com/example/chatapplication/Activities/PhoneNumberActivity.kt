package com.example.chatapplication.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivityPhoneNumberBinding
import com.google.firebase.auth.FirebaseAuth

class PhoneNumberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPhoneNumberBinding
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_number)

        binding = ActivityPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.phoneBox.requestFocus()
        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            startActivity(Intent(this@PhoneNumberActivity, MainActivity::class.java))
            finish()
        }
        binding.continueBtn.setOnClickListener{
            val intent = Intent(this, OTPActivity::class.java)
            intent.putExtra("phoneNumber", binding.phoneBox.text.toString())
            startActivity(intent)
        }

    }
}