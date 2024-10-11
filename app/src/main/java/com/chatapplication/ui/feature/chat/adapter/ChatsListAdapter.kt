package com.chatapplication.ui.feature.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chatapplication.databinding.LayoutChatsListBinding
import com.chatapplication.ui.feature.chat.model.ChatList

class ChatsListAdapter(private val onClick : (String) -> Unit): ListAdapter<ChatList, ChatsListAdapter.ChatViewHolder>(ChatDiffCallback()) {

    class ChatViewHolder(private val binding: LayoutChatsListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatList) {
            binding.tvName.text = chat.name
            binding.tvMessage.text = chat.message
            binding.tvTimeStamp.text = chat.timeStamp
            binding.tvChats.text = chat.unreadMessageCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = LayoutChatsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
        holder.itemView.setOnClickListener {onClick(chat.name)}
    }

    class ChatDiffCallback : DiffUtil.ItemCallback<ChatList>() {
        override fun areItemsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ChatList, newItem: ChatList): Boolean {
            return oldItem == newItem
        }
    }

}

//class ChatsListAdapter(private val chatList: List<ChatList>) : RecyclerView.Adapter<ChatsListAdapter.ChatListViewHolder>() {
//
//    inner class ChatListViewHolder(private val binding: LayoutChatsListBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(chat: ChatList) {
//            binding.tvName.text = chat.name
//            binding.tvMessage.text = chat.message
//            binding.tvTimeStamp.text = chat.timeStamp
//            binding.tvChats.text = if (chat.unreadMessageCount > 0) chat.unreadMessageCount.toString() else ""
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListViewHolder {
//        val binding = LayoutChatsListBinding.inflate(
//            LayoutInflater.from(parent.context), parent, false
//        )
//        return ChatListViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ChatListViewHolder, position: Int) {
//        holder.bind(chatList[position])
//    }
//
//    override fun getItemCount(): Int = chatList.size
//}