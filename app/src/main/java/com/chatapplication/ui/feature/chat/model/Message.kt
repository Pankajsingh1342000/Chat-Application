package com.chatapplication.ui.feature.chat.model

data class Message(
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
