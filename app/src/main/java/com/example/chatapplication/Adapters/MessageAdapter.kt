package com.example.chatapplication.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.Models.Message
import com.example.chatapplication.R
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList

class MessageAdapter(val context : Context, val messagesList: ArrayList<Message>, senderRoom : String, recieverRoom : String):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_SENT: Int = 2
    private val ITEM_RECIEVE: Int = 1

//    lateinit var senderRoom : String
//    lateinit var recieverRoom : String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if(viewType == 1){
            val view : View = LayoutInflater.from(context).inflate(R.layout.item_recieve, parent, false)
            return RecieveViewHolder(view)
        }
        else{
            val view : View = LayoutInflater.from(context).inflate(R.layout.item_send, parent, false)
            return SendViewHolder(view)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messagesList[position]

        if(holder.javaClass == SendViewHolder::class.java){
            val viewHolder = holder as SendViewHolder
            viewHolder.sentmessage.text = currentMessage.message

        }
        else{
            val viewHolder = holder as RecieveViewHolder
            viewHolder.recievemessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messagesList[position]

        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            ITEM_SENT
        }else{
            ITEM_RECIEVE
        }

    }

    override fun getItemCount(): Int {
        return messagesList.size
    }
}

class SendViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val sentmessage : TextView = itemView.findViewById(R.id.sendMessage)
//    val time : TextView = itemView.findViewById(R.id.msgTime)
}

class RecieveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val recievemessage : TextView = itemView.findViewById(R.id.recieveMessage)
//    val time : TextView = itemView.findViewById(R.id.msgTime)
}