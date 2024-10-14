package com.chatapplication.ui.feature.chat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chatapplication.ui.feature.chat.model.ChatList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChatListViewModel: ViewModel() {



    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _chatList = MutableLiveData<List<ChatList>>()
    val chatList: LiveData<List<ChatList>> get() = _chatList

    // Fetch user's chat list from Firestore
    fun fetchChatList(userId: String) {
        firestore.collection("users").document(userId).collection("contacts")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val chats = snapshot.documents.map { doc ->
                        val name = doc.getString("name") ?: ""
                        val phoneNumber = doc.getString("phoneNumber") ?: ""
                        val chatId = doc.id
                        val timeStamp = doc.getLong("timestamp")?.toString() ?: ""
                        ChatList(chatId, name, "", timeStamp, 0) // Sample data, adjust as needed
                    }
                    _chatList.value = chats
                }
            }
    }

//    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
//    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
//    private val _chatList = MutableLiveData<List<ChatList>>()
//    val chatList: LiveData<List<ChatList>> = _chatList
//
//    fun fetchChatList() {
//        val userId = firebaseAuth.currentUser?.uid ?: return
//        firestore.collection("chats")
//            .whereArrayContains("participants", userId)
//            .addSnapshotListener { snapshot, e ->
//                if (e != null) {
//                    return@addSnapshotListener
//                    // Handle Error
//                }
//
//                val chatList = mutableListOf<ChatList>()
//                for (doc in snapshot!!.documents) {
//                    val chat = doc.toObject(ChatList::class.java)
//                    chat?.let { chatList.add(it) }
//                }
//                _chatList.postValue(chatList)
//            }
//    }

}