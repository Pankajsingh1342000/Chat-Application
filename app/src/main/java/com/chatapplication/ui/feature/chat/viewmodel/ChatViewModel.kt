package com.chatapplication.ui.feature.chat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chatapplication.ui.feature.chat.model.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatViewModel: ViewModel() {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val _message = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> = _message

    // Fetch messages for a specific chat
    fun fetchMessages(chatId: String) {
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .addSnapshotListener { snapshot, e ->
                if (e != null){
                    // Handle error
                    return@addSnapshotListener
                }
                val messageList = mutableListOf<Message>()
                for (doc in snapshot!!.documents) {
                    val message = doc.toObject(Message::class.java)
                    message?.let { messageList.add(it) }
                }
                _message.postValue(messageList)
            }
    }

    // Send a message
    fun sendMessage(chatId: String, messageContent: String) {
        val message = Message(content = messageContent)
        firestore.collection("chats")
            .document(chatId)
            .collection("messages")
            .add(message)
            .addOnSuccessListener {
                // Message sent successfully
                Log.d("ChatViewModel", "Message sent successfully")
            }
            .addOnFailureListener {
                // Handle error in sending message
                Log.e("ChatViewModel", "Error sending message", it)
            }
    }

    // Get current user ID
    fun getCurrentUserId(): String {
        // Return the current user ID from your authentication setup
        return firebaseAuth.currentUser.toString() // Replace with actual user ID logic
    }
    // Add new chat to Firestore
    fun addNewChat(chatId: String, chatData: Map<String, Any?>) {
        firestore.collection("chats").document(chatId).set(chatData)
    }
}