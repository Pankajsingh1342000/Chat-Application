package com.example.chatapplication.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devlomi.circularstatusview.CircularStatusView
import com.example.chatapplication.Activities.MainActivity
import com.example.chatapplication.Models.UserStatus
import com.example.chatapplication.R
import com.google.android.gms.dynamic.SupportFragmentWrapper
import de.hdodenhof.circleimageview.CircleImageView
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.callback.StoryClickListeners
import omari.hamza.storyview.model.MyStory

class TopStatusAdapter(val context : Context, var userStatuses : ArrayList<UserStatus>): RecyclerView.Adapter<TopStatusViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopStatusViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_status, parent, false)
        return TopStatusViewHolder(view)

    }

    override fun onBindViewHolder(holder: TopStatusViewHolder, position: Int) {

        val userStatus : UserStatus = userStatuses[position]
        val lastStaus = userStatus.statuses.get(userStatus.statuses.size - 1)

        Glide.with(context).load(lastStaus.imageUrl).into(holder.statusprofile)
        holder.CircularStatusView.setPortionsCount(userStatus.statuses.size)

        holder.CircularStatusView.setOnClickListener {
            val myStories = ArrayList<MyStory>()
            for (status in userStatus.statuses){
                myStories.add(MyStory(status.imageUrl))
            }

            StoryView.Builder((context as MainActivity).supportFragmentManager)
                .setStoriesList(myStories)
                .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                .setTitleText(userStatus.userName) // Default is Hidden
                .setSubtitleText("") // Default is Hidden
                .setTitleLogoUrl(userStatus.profileImage) // Default is Hidden
                .setStoryClickListeners(object : StoryClickListeners{
                    override fun onDescriptionClickListener(position: Int) {

                    }

                    override fun onTitleIconClickListener(position: Int) {

                    }
                }).build().show()

        }

    }

    override fun getItemCount(): Int {
        return userStatuses.size
    }

}
class TopStatusViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    val CircularStatusView : CircularStatusView = itemView.findViewById(R.id.circular_status_view)
    val statusprofile : CircleImageView = itemView.findViewById(R.id.statusprofile)
}