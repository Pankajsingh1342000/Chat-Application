package com.chatapplication.ui.feature.chat.model

data class ChatList(
    val chatId: String = "",
    val name: String = "",
    val lastMessage: String = "",
    val timeStamp: String = "0L",
    val unreadMessageCount: Int = 0,
    val profileImage: String? = null
)
