package com.chatapplication.ui.feature.chat.model

data class Message(
    val messageText: String = "",
    val senderId: String = "",
    val timestamp: Long = 0L
)
