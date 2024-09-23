package com.chatapplication.ui.feature.chat.model

data class ChatList(
    val name: String,
    val message: String,
    val timeStamp: String,
    val unreadMessageCount: Int
)
