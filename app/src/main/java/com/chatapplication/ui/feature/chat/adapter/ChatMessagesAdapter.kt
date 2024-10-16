package com.chatapplication.ui.feature.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chatapplication.databinding.ItemMessageRecievedBinding
import com.chatapplication.databinding.ItemMessageSentBinding
import com.chatapplication.ui.feature.chat.model.Message
import com.google.firebase.auth.FirebaseAuth

class ChatMessagesAdapter(
    private var messageList: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    companion object {
        const val VIEW_TYPE_SENT = 1
        const val VIEW_TYPE_RECEIVED = 2
    }

    inner class SentMessageViewHolder(private val binding: ItemMessageSentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.messageText.text = message.messageText
            binding.timestamp.text = message.timestamp.toString()
        }
    }

    inner class ReceivedMessageViewHolder(private val binding: ItemMessageRecievedBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.messageText.text = message.messageText
            binding.timestamp.text = message.timestamp.toString()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].senderId == currentUserId) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ItemMessageSentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentMessageViewHolder(binding)
        } else {
            val binding = ItemMessageRecievedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            (holder as SentMessageViewHolder).bind(messageList[position])
        } else {
            (holder as ReceivedMessageViewHolder).bind(messageList[position])
        }
    }

    override fun getItemCount(): Int = messageList.size

    // Method to update the message list and notify the adapter
    fun updateList(newMessages: List<Message>) {
        messageList = newMessages
        notifyDataSetChanged()
    }
}