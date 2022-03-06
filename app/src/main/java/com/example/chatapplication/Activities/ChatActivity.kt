package com.example.chatapplication.Activities

import android.icu.text.DateFormat.getTimeInstance
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapplication.Adapters.MessageAdapter
import com.example.chatapplication.Models.Message
import com.example.chatapplication.R
import com.example.chatapplication.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.DateFormat.getTimeInstance
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var adapter : MessageAdapter
    private lateinit var messagesList : ArrayList<Message>
//    private lateinit var senderRoom : String
//    private lateinit var recieverRoom : String
    private lateinit var database: DatabaseReference

    var receiverRoom : String? = null
    var senderRoom : String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val recieverUid = intent.getStringExtra("uid")
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        database = FirebaseDatabase.getInstance().reference

        senderRoom = recieverUid + senderUid
        receiverRoom = senderUid + recieverUid

        messagesList = ArrayList()
        adapter = MessageAdapter(this, messagesList, senderRoom!!, receiverRoom!!)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter


        database.child("chats")
            .child(senderRoom!!)
            .child("message")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    messagesList.clear()
                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        message?.messageId = postSnapshot.key
                        messagesList.add(message!!)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        binding.sendBtn.setOnClickListener {
            val message = binding.messageBox.text.toString()

            val calendar = Calendar.getInstance()
            val simpleDateFormat = SimpleDateFormat("hh:mm aaa")
            val dateTime = simpleDateFormat.format(calendar.time).toString()

            val messageobject = Message(message, senderUid!!,dateTime)

            val randomKey : String? = database.push().key

            val lastMsgObj = HashMap<String, String>()
            lastMsgObj.put("lastMsg", messageobject.message!!)
            lastMsgObj.put("lastMsgTime", messageobject.time!!)
            database.child("chats").child(senderRoom!!).updateChildren(lastMsgObj as Map<String, String>)
            database.child("chats").child(receiverRoom!!).updateChildren(lastMsgObj as Map<String, String>)

            database.child("chats")
                .child(senderRoom!!)
                .child("message")
                .child(randomKey!!)
                .setValue(messageobject)
                .addOnSuccessListener {
                    database.child("chats")
                        .child(receiverRoom!!)
                        .child("messages")
                        .child(randomKey)
                        .setValue(messageobject)
                }
            binding.messageBox.setText("")
        }


        supportActionBar?.title = name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}