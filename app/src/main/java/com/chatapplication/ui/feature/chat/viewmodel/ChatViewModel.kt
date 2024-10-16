package com.chatapplication.ui.feature.chat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chatapplication.ui.feature.chat.model.Message
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatViewModel: ViewModel() {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    // Load chat messages from Firestore in real-time
    fun loadMessages(chatId: String) {
        firestore.collection("chats").document(chatId).collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val messageList = snapshot.documents.map { doc ->
                        val messageText = doc.getString("messageText") ?: ""
                        val senderId = doc.getString("senderId") ?: ""
                        val timestamp = doc.getLong("timestamp") ?: 0L
                        Message(messageText, senderId, timestamp)
                    }
                    _messages.value = messageList
                }
            }
    }

    // Send a new message to Firestore
    fun sendMessage(chatId: String, messageText: String, senderId: String) {
        val message = hashMapOf(
            "messageText" to messageText,
            "senderId" to senderId,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("chats").document(chatId).collection("messages")
            .add(message)
            .addOnSuccessListener {
                // Message sent successfully
            }
            .addOnFailureListener {
                // Handle failure in sending message
            }
    }
}