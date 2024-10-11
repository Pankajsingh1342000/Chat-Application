package com.chatapplication.ui.feature.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chatapplication.databinding.LayoutChatsListBinding
import com.chatapplication.databinding.LayoutMessageItemBinding
import com.chatapplication.ui.feature.chat.model.Message

class ChatMessagesAdapter : RecyclerView.Adapter<ChatMessagesAdapter.MessageViewHolder>() {
    private val message: MutableList<Message> = mutableListOf()

    class MessageViewHolder(private val binding: LayoutMessageItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message) {
            binding.tvMessage.text = message.content
            binding.tvTime.text = message.timestamp.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = LayoutMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int = message.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(message[position])
    }

    fun submitList(newMessage: List<Message>){
        message.clear()
        message.addAll(newMessage)
        notifyDataSetChanged()
    }

}