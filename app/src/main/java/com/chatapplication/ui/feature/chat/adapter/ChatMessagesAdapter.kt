package com.chatapplication.ui.feature.chat.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.chatapplication.databinding.LayoutChatsListBinding
import com.chatapplication.databinding.LayoutMessageItemBinding
import com.chatapplication.ui.feature.chat.model.Message

class ChatMessagesAdapter(
    private val currentUserId: String
) : RecyclerView.Adapter<ChatMessagesAdapter.ChatMessageViewHolder>() {

    private val messageList = mutableListOf<Message>()

    inner class ChatMessageViewHolder(private val binding: LayoutMessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.tvMessage.text = message.messageText
            binding.tvTime.text = message.timestamp.toString()

            // Adjust message appearance based on sender (current user or contact)
            if (message.senderId == currentUserId) {
                // Align to the right (sent by the user)
                binding.root.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
            } else {
                // Align to the left (received)
                binding.root.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val binding = LayoutMessageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ChatMessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    override fun getItemCount(): Int = messageList.size

    fun submitList(newMessages: List<Message>) {
        messageList.clear()
        messageList.addAll(newMessages)
        notifyDataSetChanged()
    }
}

//class ChatMessagesAdapter : RecyclerView.Adapter<ChatMessagesAdapter.MessageViewHolder>() {
//    private val message: MutableList<Message> = mutableListOf()
//
//    class MessageViewHolder(private val binding: LayoutMessageItemBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(message: Message) {
//            binding.tvMessage.text = message.content
//            binding.tvTime.text = message.timestamp.toString()
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
//        val binding = LayoutMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MessageViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int = message.size
//
//    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
//        holder.bind(message[position])
//    }
//
//    fun submitList(newMessage: List<Message>){
//        message.clear()
//        message.addAll(newMessage)
//        notifyDataSetChanged()
//    }
//
//}