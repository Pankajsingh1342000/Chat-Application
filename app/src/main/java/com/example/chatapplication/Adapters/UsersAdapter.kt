package com.example.chatapplication.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapplication.Activities.ChatActivity
import com.example.chatapplication.Models.User
import com.example.chatapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersAdapter(val context : Context, private val users: ArrayList<User>) : RecyclerView.Adapter<UsersViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_conversation, parent, false)
        return UsersViewHolder(view)

    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {

        val user = users[position]

        val senderId : String? = FirebaseAuth.getInstance().uid
        val senderRoom : String = senderId + user.uid
        FirebaseDatabase.getInstance().reference
            .child("chats")
            .child(senderRoom)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val lastMsg: String = snapshot.child("lastMsg").value.toString()
                        val time: String = snapshot.child("lastMsgTime").value.toString()
                        holder.lastmsg.text = lastMsg
                        holder.msgTime.text = time
                    }else{
                        holder.lastmsg.text = "Tap to Chat"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        holder.username.text = user.name.toString()

        Glide.with(context).load(user.profileImage)
            .placeholder(R.drawable.avatar)
            .into(holder.profileImage)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", user.name.toString())
            intent.putExtra("uid", user.uid)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return users.size
    }

}

class UsersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val username : TextView = itemView.findViewById(R.id.username)
    val profileImage : ImageView = itemView.findViewById(R.id.profile)
    val lastmsg : TextView = itemView.findViewById(R.id.lastMsg)
    val msgTime : TextView = itemView.findViewById(R.id.msgTime)


}