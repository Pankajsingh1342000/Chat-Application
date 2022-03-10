package com.example.chatapplication.Models

class UserStatus {

    lateinit var userName : String
    lateinit var profileImage : String
    lateinit var lastUpdated : String
    lateinit var statuses : ArrayList<Status>
    constructor(){}
    constructor(
        userName: String,
        profileImage: String,
        lastUpdated: String,
        statuses: ArrayList<Status>
    ) {
        this.userName = userName
        this.profileImage = profileImage
        this.lastUpdated = lastUpdated
        this.statuses = statuses
    }


}