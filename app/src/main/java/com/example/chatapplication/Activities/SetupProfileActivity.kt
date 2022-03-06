package com.example.chatapplication.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.chatapplication.R
import com.example.chatapplication.Models.User
import com.example.chatapplication.databinding.ActivitySetupProfileBinding
import com.example.chatapplication.utils.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SetupProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupProfileBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private var selectedImage : Uri? = null
    private var loading = LoadingDialog(this,"Updating profile...")

    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile)

        supportActionBar?.hide()

        binding = ActivitySetupProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        var database = FirebaseDatabase.getInstance().reference
        storage  = FirebaseStorage.getInstance()

        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                binding.profileimage.setImageURI(it)
                selectedImage = it
            }
        )


        binding.profileimage.setOnClickListener {
            getImage.launch("image/*")
        }

        binding.continueBtn.setOnClickListener {

            val name : String = binding.nameBox.text.toString()

            if(name.isEmpty()){
                binding.nameBox.error = "Please Type a name"
            }
            else{
                loading.startLoading()
                if(selectedImage != null){

                    val reference : StorageReference = storage.reference.child("Profiles").child(auth.uid!!)
                    reference.putFile(selectedImage!!).addOnCompleteListener{ task ->
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()

                        if(task.isSuccessful){

                            reference.downloadUrl.addOnSuccessListener {uri ->
                                val imageUrl : String = uri.toString()
                                val uid : String? = auth.uid!!
                                val phone : String? = auth.currentUser?.phoneNumber
                                val name : String = binding.nameBox.text.toString()
                                val user = User(uid, name, phone , imageUrl)

                                database.child("users").child(uid!!).setValue(user).addOnSuccessListener {
                                    loading.dismiss()
                                    startActivity(Intent(this@SetupProfileActivity, MainActivity::class.java))
                                    finish()
                                }
                            }
                        }
                    }
                }

                else{

                    val uid : String? = auth.uid!!
                    val phone : String? = auth.currentUser?.phoneNumber
                    val name : String = binding.nameBox.text.toString()
                    val user = User(uid, name, phone , "No Image")

                    database.child("users").child(uid!!).setValue(user).addOnSuccessListener {
                        loading.dismiss()
                        startActivity(Intent(this@SetupProfileActivity, MainActivity::class.java))
                        finish()
                    }

                }
            }
        }
    }
}