package com.chatapplication.ui.feature.chat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chatapplication.ui.feature.chat.model.Message
import com.google.firebase.auth.FirebaseAuth
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
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val messageList = snapshot.documents.map { doc ->
                        val messageText = doc.getString("messageText") ?: ""
                        val senderId = doc.getString("senderId") ?: ""
                        val timestamp = doc.getLong("timestamp") ?: 0L
                        Message(
                            messageText = messageText,
                            senderId = senderId,
                            timestamp = timestamp
                        )
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
                // Message sent successfully, handle UI update if needed
            }
            .addOnFailureListener {
                // Handle failure in sending message
            }
    }


//    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
//    private val _message = MutableLiveData<List<Message>>()
//    val messages: LiveData<List<Message>> = _message
//
//    // Fetch messages for a specific chat
//    fun fetchMessages(chatId: String) {
//        firestore.collection("chats")
//            .document(chatId)
//            .collection("messages")
//            .addSnapshotListener { snapshot, e ->
//                if (e != null){
//                    // Handle error
//                    return@addSnapshotListener
//                }
//                val messageList = mutableListOf<Message>()
//                for (doc in snapshot!!.documents) {
//                    val message = doc.toObject(Message::class.java)
//                    message?.let { messageList.add(it) }
//                }
//                _message.postValue(messageList)
//            }
//    }
//
//    // Send a message
//    fun sendMessage(chatId: String, messageContent: String) {
//        val message = Message(content = messageContent)
//        firestore.collection("chats")
//            .document(chatId)
//            .collection("messages")
//            .add(message)
//            .addOnSuccessListener {
//                // Message sent successfully
//                Log.d("ChatViewModel", "Message sent successfully")
//            }
//            .addOnFailureListener {
//                // Handle error in sending message
//                Log.e("ChatViewModel", "Error sending message", it)
//            }
//    }
//
//    // Get current user ID
//    fun getCurrentUserId(): String {
//        // Return the current user ID from your authentication setup
//        return firebaseAuth.currentUser.toString() // Replace with actual user ID logic
//    }
//    // Add new chat to Firestore
//    fun addNewChat(chatId: String, chatData: Map<String, Any?>) {
//        firestore.collection("chats").document(chatId).set(chatData)
//    }
}