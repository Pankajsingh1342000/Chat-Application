package com.example.chatapplication.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.chatapplication.Adapters.UsersAdapter
import com.example.chatapplication.R
import com.example.chatapplication.Models.User
import com.example.chatapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var database = FirebaseDatabase.getInstance().reference
    private lateinit var users : ArrayList<User>
    private lateinit var usersAdapter : UsersAdapter
    var auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        users = ArrayList()

        usersAdapter = UsersAdapter(this, users)
        binding.recyclerView.adapter = usersAdapter

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
}