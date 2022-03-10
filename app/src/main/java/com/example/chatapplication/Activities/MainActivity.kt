package com.example.chatapplication.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.chatapplication.Adapters.TopStatusAdapter
import com.example.chatapplication.Adapters.UsersAdapter
import com.example.chatapplication.Models.Status
import com.example.chatapplication.R
import com.example.chatapplication.Models.User
import com.example.chatapplication.Models.UserStatus
import com.example.chatapplication.databinding.ActivityMainBinding
import com.example.chatapplication.utils.LoadingDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var database = FirebaseDatabase.getInstance().reference
    private lateinit var users : ArrayList<User>
    private lateinit var usersAdapter : UsersAdapter
    var auth = FirebaseAuth.getInstance()
    private lateinit var statusAdapter : TopStatusAdapter
    private lateinit var userStatuses : ArrayList<UserStatus>
    val storage = FirebaseStorage.getInstance()
    private var loading = LoadingDialog(this,"Uploading...")
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        users = ArrayList()
        userStatuses = ArrayList()

        usersAdapter = UsersAdapter(this, users)
        statusAdapter = TopStatusAdapter(this, userStatuses)

        binding.recyclerView.adapter = usersAdapter
        binding.statusList.adapter = statusAdapter


        database.child("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid!!)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    user = snapshot.getValue(User::class.java)!!
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })



        val dateTime = dateTime()
        var selectedImage : Uri? = null


        val getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {image ->
                selectedImage = image

                if (selectedImage != null){

                    loading.startLoading()
                    val reference : StorageReference = storage.reference.child("status").child(dateTime)
                    reference.putFile(selectedImage!!).addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            reference.downloadUrl.addOnSuccessListener {Uri ->
                                val userStatus = UserStatus()
                                userStatus.userName = user.name.toString()
                                userStatus.profileImage = user.profileImage!!
                                userStatus.lastUpdated = dateTime

                                val obj = HashMap<String, String>()
                                obj.put("name", userStatus.userName)
                                obj.put("profileImage", userStatus.profileImage)
                                obj.put("lastUpdated", userStatus.lastUpdated)

                                val imageUrl : String = Uri.toString()
                                val status = Status(imageUrl, userStatus.lastUpdated)

                                database.child("status")
                                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .updateChildren(obj as Map<String, String>)

                                database.child("status")
                                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                                    .child("statuses")
                                    .push()
                                    .setValue(status)

                                loading.dismiss()
                            }
                        }
                    }

                }
            }
        )

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.status ->{
                    getImage.launch("image/*")
                }
            }

            return@setOnItemSelectedListener true
        }


        database.child("users").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                for(postSnapshot in snapshot.children){
                    val user = postSnapshot.getValue(User::class.java)
                    if(auth.currentUser?.uid != user?.uid){
                        users.add(user!!)
                    }
                }
                usersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        database.child("status").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    userStatuses.clear()
                    for(postSnapshot in snapshot.children){
                        val status = UserStatus()
                        status.userName = postSnapshot.child("name").value.toString()
                        status.profileImage = postSnapshot.child("profileImage").value.toString()
                        status.lastUpdated = postSnapshot.child("lastUpdated").value.toString()

                        var statuses = ArrayList<Status>()

                        for (statusSnapshot in postSnapshot.child("statuses").children){

                            val sampleStatus = statusSnapshot.getValue(Status::class.java)
                            statuses.add(sampleStatus!!)

                        }
                        status.statuses = statuses
                        userStatuses.add(status)
                    }
                    statusAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.search -> Toast.makeText(this, "search clicked", Toast.LENGTH_SHORT).show()
            R.id.setting -> Toast.makeText(this, "setting clicked", Toast.LENGTH_SHORT).show()

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.topmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun dateTime(): String{
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("hh:mm aaa")
        val dateTime = simpleDateFormat.format(calendar.time).toString()
        return dateTime
    }

}